import akka.actor.*

class DocScanner(val nLines : Int, var queues : List) extends Actor {
	val numLines = nLines
	val nextLine = 0
	var random = new Random()
	
	def receive = {
		case GetPassenger =>
			if(random.nextInt(100) > 20) {
				queues[nextLine] ! ToLine(GetPassenger.passenger)
				println("Passenger " + GetPassenger.passenger + " is sent to queue " + nextLine + " by the Doc Scanner.")
			} else {
				println("Passenger " + GetPassenger.passenger + " has invalid documentation.")
			}
	}
}