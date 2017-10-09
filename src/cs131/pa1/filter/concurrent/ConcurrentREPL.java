package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;

import java.util.*;


public class ConcurrentREPL {

    private static Map<Thread, String> listOfCommands = new HashMap<Thread, String>();
	static String currentWorkingDirectory;

	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		while(true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
            //if user asks for repl jobs go trough the list of commands and check which one is done
			if (command.equals("repl_jobs")){
				int counter = 1;
				for (Thread thread: listOfCommands.keySet()) {
                	if (thread.isAlive()){
						System.out.println("\t" + counter + ". " +listOfCommands.get(thread));
						counter++;

					}
                }
			} else if(command.equals("exit")) {
				break;
			} else if(!command.trim().equals("")) {
				//building the filters list from the command
				List <ConcurrentFilter> filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				//TODO: thread.join
				if (filterlist != null) {
					Iterator itr = filterlist.iterator();
					for (int i = 0; i<filterlist.size()-1;i++) {
						Thread thread = new Thread((ConcurrentFilter) itr.next());
						thread.start();

						//if user chose to do stuff concurrently add that command to the list of jobs else proceed as is
						if (!command.substring(command.length()-1).equals("&")) {
							try {
								thread.join();
							} catch (InterruptedException e) {

							}
						}
					}

					Thread t = new Thread((ConcurrentFilter) itr.next());
					t.start();

					if (command.endsWith("&")) {
						listOfCommands.put(t, command);
					} else {
						try {
							t.join();
						} catch (InterruptedException e) {

						}
					}
				}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}

}
