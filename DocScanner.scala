// Author: Emma Nelsons
import akka.actor.Actor
import akka.actor.ActorRef
import scala.util.Random
import scala.collection.mutable.MutableList
import akka.actor.PoisonPill

// Gets a passenger from the Controller, decides whether or not the
// passenger's documentation is valid (80% chance it is) and sends them
// on to the next queue. If not, they are turned away.
class DocScanner(val numLines : Int) extends Actor {
	var nextLine = 0
	val random = new Random()
	var bodyScanners = MutableList[ActorRef]()
	var bagScanners = MutableList[ActorRef]()
	
	def receive = {
		case SendScanners(bodyS, bagS) =>
			bodyScanners = bodyS
			bagScanners = bagS

		case GetPassenger(passenger) =>
			if(random.nextInt(100) > 20) {
				bodyScanners.get(nextLine).head ! ToLine(passenger)
				println("Passenger " + passenger + " is sent to wait for Body Scanner " + nextLine + " by the Doc Scanner.")
				bagScanners.get(nextLine).head ! ToLine(passenger)
				println("Passenger " + passenger + "'s bag is sent to wait for Bag Scanner " + nextLine + " by the Doc Scanner.")
				nextLine = (nextLine + 1) % numLines
			} else {
				println("Passenger " + passenger + " has invalid documentation.")
			}
	}

	override def poststop = {
		for(i <- 0 to numLines-1) {
			bodyScanners.get(i).head ! PoisonPill
			bagScanners.get(i).head ! PoisonPill
		}
	}
}