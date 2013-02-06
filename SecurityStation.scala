// Author: Dan Lavoie
// Editied by: Emma Nelson
import akka.actor.Actor
import akka.actor.ActorRef
import scala.collection.mutable.MutableList
import akka.actor.Props
import akka.actor.ActorSystem

//The security station processes passengers who move through the system.
//It keeps track of bags and people sent through its line's scanners,
//and processes whether to send them through or put them in the jail.
//At the end of the day, it checks if it can close by waiting for its
//associated scanners to close.
class SecurityStation(val line : Int, val jail : ActorRef) extends Actor {
	val stationLine = line
	var passengers = new MutableList[Tuple2[Int, Boolean]]();
	val system = ActorSystem("Test")
	var bagScanner = system.actorOf(Props(new DefaultActor()))
	var bodyScanner = system.actorOf(Props(new DefaultActor()))
	var oneClosed = false
	
	def receive = {
		case SendBagScanner(bs) =>
			bagScanner = bs

		case SendBodyScanner(bs) =>
			bodyScanner = bs

		case BagToSecurityStation(bag) =>
			// When the bag scanner sends a bag to the security station
			println("Bag " + bag._1 + " arrived at Security Station.")
			checkPassengerList(bag)

		case PersonToSecurityStation(passenger) =>
			println("Passenger " + passenger._1 + " arrived at Security Station.")
			checkPassengerList(passenger)

		case ScannerClosed(id) =>
			// When both scanners have sent the message, it can kill itself
			// As each scanner sends the message, it replies with a poisonpill or stop call
			println("Scanner " + id + " closed")
			if(!oneClosed) {
				oneClosed = true
			} else {
				//stop()
			}
	}

	def checkPassengerList(passenger : Tuple2[Int, Boolean]) = {
		// Check if the passenger or bag has already been partially processed.
		// To do this, iterate through the list. If there is a tuple with the same first int,
		// then an entry exists. Otherwise, it does not.
		if (passengers.length > 0) {
			//Create the boolean to check if an entry is found
			var entryFound = false
			var entryIndex = -1
			for (i <- 0 to passengers.length-1) {
				if(passengers.get(i).head._1 == passenger._1) {
					entryFound = true
					entryIndex = i
				}
			}
			// If we found the passenger in the list, finish processing them
			if (entryFound) {
				// Check if one of the entries didn't pass the security scan
				if(passengers.get(entryIndex).head._2 == false || passenger._2 == false) {
					// Something didn't pass. To Jail!
					println("Passenger " + passenger._1 + " failed the security check and heads to jail.")
					// We need to remove the entry in the list. Since MutableList can't "remove()" an item,
					// we do some slicing to make it work.
					val tempList = passengers.slice(0, entryIndex)
					val tempList2 = passengers.slice(entryIndex+1, passengers.length)
					passengers = tempList ++ tempList2
					// Send the passenger to jail
					jail ! Prisoner(passenger._1)
				} else {
					println("Passenger " + passenger._1 + " passed the security check and heads to a flight.")
					// We need to remove the entry in the list. Since MutableList can't "remove()" an item,
					// we do some slicing to make it work.
					val tempList = passengers.slice(0, entryIndex)
					val tempList2 = passengers.slice(entryIndex+1, passengers.length)
					passengers = tempList ++ tempList2
				}
			} else {
				// If we didn't find them in the list, add them to the list
				passengers = passengers += passenger
			}
		} else {
			passengers = passengers += passenger
		}
	}
}

// An empty actor that holds the place for a real actor to be saved to a
// variable later.
class DefaultActor() extends Actor {
	def receive = {
		case EndDay =>
			println("Illegal message sent.")
	}
}