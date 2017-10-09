package cs131.pa1.filter.concurrent;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import cs131.pa1.filter.Filter;


public abstract class ConcurrentFilter extends Filter implements Runnable {

	protected Queue<String> input;
	protected Queue<String> output;
	protected String done = "JA SAM ZAVRSIO";
	
	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}
	
	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				this.output = new LinkedBlockingQueue<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	
	public Filter getNext() {
		return next;
	}
	
	public void process(){
		while (!input.isEmpty()){
			String line = input.poll();
			String processedLine = processLine(line);
			if (processedLine != null){
				output.add(processedLine);
			}
		}	
	}
	
	@Override
	public boolean isDone() {
		return input != null && input.peek().equals(done);
//		//TODO: check if it removes the stuff from the queue
//		if (output != null) {
//			Iterator iter = output.iterator();
//
//			while (iter.hasNext()) {
//				Object iter1 = iter.next();
//				if (!iter1.equals(done)) {
//					System.out.println("IS DONE FALSE");
//					return true;
//				}
//			}
//		}
//		return false;
	}





//		if (output != null) {
//			try {
//				for (String string : output) {
//					if (!string.equals(done)) {
//						return false;
//					}
//				}
//			} catch (NullPointerException e) {
//				System.out.println("Null pointer");
//			}
//
////		}
//		return true;
	protected abstract String processLine(String line);

	public Queue returnOutput(){
		return this.output;
	}
}
