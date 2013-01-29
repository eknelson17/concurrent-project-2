//Author: Dan Lavoie
import akka.actor.Actor
import akka.actor.ActorRef
import scala.collection.mutable.MutableList

//The security station processes passengers who move through the system.
//It keeps track of bags and people sent through its line's scanners,
//and processes whether to send them through or put them in the jail.
//At the end of the day, it checks if it can close by waiting for its
//associated scanners to close.
class SecurityStation(val line : Int) extends Actor {
	val stationLine = line
	var passengers = new MutableList();
	

}