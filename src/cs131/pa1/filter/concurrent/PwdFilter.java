package cs131.pa1.filter.concurrent;

public class PwdFilter extends ConcurrentFilter {
	public PwdFilter() {
		super();
	}
	
	public void process() {
		output.add(processLine(""));
	}
	
	public String processLine(String line) {
		return ConcurrentREPL.currentWorkingDirectory;
	}

	@Override
	public void run() {
		process();
		output.add(done);
	}
}
