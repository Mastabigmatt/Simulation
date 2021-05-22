



/**
 *	Machine in a factory
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class Server extends Machine implements CProcess,ProductAcceptor
{
	private double std;

	/**
	*	Constructor
	*        Service times are exponentially distributed with mean 30
	*	@param q	Queue from which the machine has to take products
	*	@param s	Where to send the completed products
	*	@param e	Eventlist that will manage events
	*	@param n	The name of the machine
	*/
	public Server(Queue q, ProductAcceptor s, CEventList e, String n)
	{
		super(q,  s,  e,  n);
		//arbitrary standard deviation
		std = 1;
	}
	
	
	public Server(Queue q,ProductAcceptor s, CEventList e, String n, int i, double std) {
		super(q,  s,  e,  n, i);
		this.std = std;
	}
	
	private void startProduction()
	{
		// generate duration
		if(meanProcTime>0)
		{
			double duration = drawRandomNormal(meanProcTime, std);
			// Create a new event in the eventlist
			double tme = eventlist.getTime();
			eventlist.add(this,0,tme+duration); //target,type,time
			// set status to busy
			status='b';
		}
		else
		{
			if(processingTimes.length>procCnt)
			{
				eventlist.add(this,0,eventlist.getTime()+processingTimes[procCnt]); //target,type,time
				// set status to busy
				status='b';
				procCnt++;
			}
			else
			{
				eventlist.stop();
			}
		}
	}   
	
	//@TODO: THIS GENERATE UNIFORM RANDOM VARIATES WE NEED NORMAL
	public static double drawRandomNormal(double mean, double std)
	{
		// draw a [0,1] uniform distributed number
		double u = Math.random();
		// Convert it into a exponentially distributed random variate with mean 33
		double res =  Math.pow(Math.log(1 - Math.random()), 2);    

		return res;
	}
}