// Author: Emma Nelson
// Edited by: Maddison Hickson
import akka.actor.Actor
import akka.actor.ActorRef
import scala.util.Random
import akka.actor.ActorSystem
import akka.actor.Props
import scala.collection.mutable.MutableList
import akka.actor.PoisonPill

// Creates and sends passengers into the system when the main tells 
// it to. Closes everything else as possible.
// class Controller(val docScanner : ActorRef, val jail : ActorRef) extends Actor {
class Controller(val system : ActorSystem, val numLines : Int) extends Actor {
	val random = new Random()
	var passNum = 1
	var jail = system.actorOf(Props(new DefaultActor()))
	var securityStations = MutableList[ActorRef]()	
	var bodyScanners = MutableList[ActorRef]()
	var bagScanners = MutableList[ActorRef]()
	var docScanner = system.actorOf(Props(new DocScanner(numLines)))

	override def preStart = {
		println("The DocScanner has been started.")

		jail = system.actorOf(Props(new Jail(numLines, self)))
		println("The Jail has been started.")

		// Create all the security stations, bag scanners, and body scanners
		// and add them to the appropriate list to be sent to the DocScanner
		for(i <- 1 to numLines) {
			// Passed i so it knows what number it is
			val securityStation = system.actorOf(Props(new SecurityStation(i, jail)))
			println("Security Station " + i + " has been started.")
			securityStations = securityStations += securityStation

			//initialize the BodyScanner and adds it to the list of BodyScanners
			val bodyScanner = system.actorOf(Props(new BodyScanner(i, securityStation)))
			println("Body Scanner " + i + " has been started.")
			bodyScanners = bodyScanners += bodyScanner
			
			//initialize the BagScanner and adds it to the list of BagScanners
			val bagScanner = system.actorOf(Props(new BagScanner(i, securityStation)))
			println("Bag Scanners " + i + " has been started.")
			bagScanners = bagScanners += bagScanner
		}

		docScanner ! SendScanners(bodyScanners, bagScanners)		
	}
	
	def receive = {
		case SendPassengers =>
			var numPass = random.nextInt(10)
			for(i <- 1 to numPass) {
				docScanner ! GetPassenger(passNum)
				println("Passenger " + passNum + " is sent to wait for the Doc Scanner by the Controller.")
				passNum = 1 + passNum
			}

		case EndDay =>
			docScanner ! PoisonPill
			println("Controller tells DocScanner to close.")

		case JailClosed =>
			println("Controller tells Jail to close.")
			jail ! PoisonPill
			println("Controller tells itself to close.")
			self ! PoisonPill
			println("System is shutting down.")
			system.shutdown()
			System.exit(0)
	}
}