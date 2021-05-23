package Simulation;



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
	
	public boolean giveProduct(Product p)
	{
		// Only accept something if the machine is idle
		if(status=='i')
		{
			// accept the product
			product=p;
			// mark starting time
			product.stamp(eventlist.getTime(),"Production job started",name);
			// start production
			startProduction();
			// Flag that the product has arrived
			return true;
		}
		// Flag that the product has been rejected
		else{
			System.out.print("system is busy");
			return false;
		}
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
	  double r, x, y;
      
      // find a uniform random point (x, y) inside unit circle
      do {
         x = 2.0 * Math.random() - 1.0;
         y = 2.0 * Math.random() - 1.0;
         r = x*x + y*y;
      } 
      while (r > 1 || r == 0);
  
      // apply the Box-Muller formula to get standard Gaussian z    
      double z = x * Math.sqrt(-2.0 * Math.log(r) / r);
      
      double norm = z*std + mean;
      if (norm <1){
    	  norm = 1;
      }
      // print it to standard output
      return norm;
   }
}
