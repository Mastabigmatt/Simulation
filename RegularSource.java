

/**
 *	A source of products
 *	This class implements CProcess so that it can execute events.
 *	By continuously creating new events, the source keeps busy.
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class RegularSource implements CProcess
{
	/** Eventlist that will be requested to construct events */
	private CEventList list;
	/** Queue that buffers products for the machine */
	private ProductAcceptor queue;
	/** Name of the source */
	private String name;
	private int nProducts;
	/** Mean interarrival time */
	private double meanArrTime;
	/** Interarrival times (in case pre-specified) */
	private double[] interarrivalTimes;
	/** Interarrival time iterator */
	private int interArrCnt;

	/**
	*	Constructor, creates objects
	*        Interarrival times are exponentially distributed with mean 33
	*	@param q	The receiver of the products
	*	@param l	The eventlist that is requested to construct events
	*	@param n	Name of object
	*/
	public RegularSource(ProductAcceptor q,CEventList l,String n)
	{
		list = l;
		queue = q;
		name = n;
		nProducts = 0;
		meanArrTime=33;
		// put first event in list for initialization
		list.add(this,0,drawNonstationaryPoissonProcess(meanArrTime)); //target,type,time
	}

	/**
	*	Constructor, creates objects
	*        Interarrival times are exponentially distributed with specified mean
	*	@param q	The receiver of the products
	*	@param l	The eventlist that is requested to construct events
	*	@param n	Name of object
	*	@param m	Mean arrival time
	*/
	public RegularSource(ProductAcceptor q,CEventList l,String n,double m)
	{
		list = l;
		queue = q;
		name = n;
		meanArrTime=m;
		// put first event in list for initialization
		list.add(this,0,drawNonstationaryPoissonProcess(meanArrTime)); //target,type,time
	}

	/**
	*	Constructor, creates objects
	*        Interarrival times are prespecified
	*	@param q	The receiver of the products
	*	@param l	The eventlist that is requested to construct events
	*	@param n	Name of object
	*	@param ia	interarrival times
	*/
	public RegularSource(ProductAcceptor q,CEventList l,String n,double[] ia)
	{
		list = l;
		queue = q;
		name = n;
		meanArrTime=-1;
		interarrivalTimes=ia;
		interArrCnt=0;
		// put first event in list for initialization
		list.add(this,0,interarrivalTimes[0]); //target,type,time
	}
	
        @Override
	public void execute(int type, double tme)
	{
		// show arrival
		System.out.println("Regular Job Arrival at time = " + tme);
		// give arrived product to queue
		Product p = new Product();
		p.stamp(tme,"Creation",name);
		queue.giveProduct(p);
		// generate duration
		if(meanArrTime>0)
		{
			double duration = drawNonstationaryPoissonProcess(meanArrTime);
			// Create a new event in the eventlist
			list.add(this,0,tme+duration); //target,type,time
		}
		else
		{
			interArrCnt++;
			if(interarrivalTimes.length>interArrCnt)
			{
				list.add(this,0,tme+interarrivalTimes[interArrCnt]); //target,type,time
			}
			else
			{
				list.stop();
			}
		}
	}


	//@TODO: THIS IS NOT CORRECT

	/**
	 * draw Non-Stationary Poisson Process
	 * sinusoid with a period of 24 hours
	 * a mean of 2 per hour
	 * an amplitude of 0.8
	 * Service times are normally distributed with mean 2 hours and 25 minutes
	 * a standard deviation of 42 minutes
	 * the smallest job size is 1 minute
	 * The mean is given for the normal distribution before truncation
	 * @param t
	 * @return
	 */
        public static double drawNonstationaryPoissonProcess(double t)    
        {
        	double lambda = 0;
        	if (t >= 3 * 60 * 60 && t < 4 * 60 * 60){        
        		lambda = 0.2 / 60;        } 
        	else {     
        			lambda = 2.0 / 60;
        	}
        	return  -Math.log(1 - Math.random()) / lambda;    
        }
	public int getnProducts() {
		return this.nProducts;
	}
}