/**
 *	Example program for using eventlists
 *	@author Joel Karel
 *	@version %I%, %G%
 */

package Simulation;

public class Simulation {

	public CEventList list;
	public Queue queue;
	public Source source;
	public Sink sink;
	public Machine mach;


	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		// Create an eventlist
		CEventList l = new CEventList();

		// A queue for the machine
		Queue GPU_q = new Queue();
		Queue regular_q = new Queue();

		// A source
		GPUSource gpu_s = new GPUSource(GPU_q, l, "GPU Source", 300);
		RegularSource regular_s = new RegularSource(regular_q, l, "Regular Source");

		// A sink
		Sink GPU_si = new Sink("GPU sink");
		Sink regular_si = new Sink("Regular sink");


		// gpu server
		Machine GPU_server1 = new ServerGPU(GPU_q, regular_q, GPU_si, l, "GPU Server 1", 240, 50);
		Machine GPU_server2 = new ServerGPU(GPU_q, regular_q, GPU_si, l, "GPU Server 2", 240, 50);

		Machine regular_server1 = new Server(regular_q, regular_si, l, "regular Server 1", 145, 42);
		Machine regular_server2 = new Server(regular_q, regular_si, l, "regular Server 2", 145, 42);
		Machine regular_server3 = new Server(regular_q, regular_si, l, "regular Server 3", 145, 42);
		Machine regular_server4 = new Server(regular_q, regular_si, l, "regular Server 4", 145, 42);
		Machine regular_server5 = new Server(regular_q, regular_si, l, "regular Server 5", 145, 42);
		Machine regular_server6 = new Server(regular_q, regular_si, l, "regular Server 6", 145, 42);

		l.start(20000); // 2000 is maximum time
		System.out.println();

		String csvFileCR = "regDataFileCreation.csv";
		String csvFileSR = "regDataFileStarted.csv";
		String csvFileDR = "regDataFileCompleted.csv";
		String csvFileCGPU = "gpuDataFileCreation.csv";
		String csvFileSGPU = "gpuDataFileStarted.csv";
		String csvFileDGPU = "gpuDataFileCompleted.csv";


		CSVM.writeCSV(regular_si, csvFileCR, csvFileSR, csvFileDR);
		CSVM.writeCSV(GPU_si, csvFileCGPU, csvFileSGPU, csvFileDGPU);


		String[] events_ = regular_si.getEvents();
		double[] times_ = regular_si.getTimes();
		String[] stations_ = regular_si.getStations();
		int[] numbers_ = regular_si.getNumbers();
		System.out.println("regular jobs \n");
		for (int i = 0; i < times_.length; i++) {
			System.out.println("number = " + numbers_[i]);
			System.out.println("event = " + events_[i]);
			System.out.println("time = " + times_[i]);
			System.out.println("station = " + stations_[i]);
			System.out.println();
		}


		String[] events = GPU_si.getEvents();
		double[] times = GPU_si.getTimes();
		String[] stations = GPU_si.getStations();
		int[] numbers = GPU_si.getNumbers();
		System.out.println("GPU jobs \n");
		for (int i = 0; i < times.length; i++) {
			System.out.println("number = " + numbers[i]);
			System.out.println("event = " + events[i]);
			System.out.println("time = " + times[i]);
			System.out.println("station = " + stations[i]);
			System.out.println();
		}

	}
}


