// Author: Emma Nelsons
import akka.actor.Actor
import akka.actor.ActorRef
import scala.util.Random
import scala.collection.mutable.MutableList

// Creates and sends passengers into the system when the main tells 
// it to. Closes everything else as possible.
//class Controller(val docScanner : ActorRef, val jail : ActorRef) extends Actor {
class Controller(val system : ActorSystem, val numLines : Int) extends Actor {
	val random = new Random()
	var passNum = 1
	var securityStations = MutableList[ActorRef]()
	var docScanner = system.actorOf(Props(new DocScanner(numLines)))

	override def prestart = {
		println("The DocScanner has been started.")

		val jail = system.actorOf(Props(new Jail()))
		println("The Jail has been started.")
		
		var bodyScanners = MutableList[ActorRef]()
		var bagScanners = MutableList[ActorRef]()

		// Create all the security stations, bag scanners, and body scanners
		// and add them to the appropriate list to be sent to the DocScanner
		for(i <- 1 to numLines) {
			// Passed i so it knows what number it is
			val securityStation = system.actorOf(Props(new SecurityStation(i)))
			println("Security Station " + i + " has been started.")
			securityStations = securityStations.+=(securityStation)

			val bodyScanner = system.actorOf(Props(new BodyScanner(i, securityStation)))
			println("Body Scanner " + i + " has been started.")
			bodyScanners = bodyScanners.+=(bodyScanner)

			val bagScanner = system.actorOf(Props(new BagScanner(i, securityStation)))
			println("Bag Scanners " + i + " has been started.")
			bagScanners = bagScanners.+=(bagScanner)
		}

		docScanner ! SendScanners(bodyScanners, bagScanners)		
	}
	
	def receive = {
		case SendPassengers =>
			var numPass = random.nextInt(100)
			for(i <- 1 to numPass) {
				docScanner ! GetPassenger(passNum)
				passNum = 1 + passNum
			}
		// TODO Insert Close operations when we have that figured out
	}
}