import akka.actor.TypedActor

//The Jail keeps a list of immutable passenger objects it is sent.
//At the end of the day, it receives a message to put its passengers
//in permanent lockup, and purges all of its passengers.
object jail extends TypedActor {
	
}