import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Coursework1.java
 * 
 * Accepts a list of hosts from command-line arguments and show the detail of
 * each host.
 */
public class Coursework1 {
	/** list of all parsed addresses */
	public static List<Address> all = new ArrayList<Address>();

	/** an inner class which encapsulate the properties of an InetAddress object */
	public static class Address {
		String host; // host name
		String ipAddr; // name of ip address
		String canonicalName; // Canonical name of ip address
		byte[] addr; // the numbers of ip address
		boolean reachable; // indicator of whether it's reachable or not
		String type; // IPV4 or IPV6
		String highWirechy;
		
		/** constructor */
		public Address(String host, String ipAddr, String canonicalName, byte[] addr, boolean reachable) {
			this.host = host;
			this.ipAddr = ipAddr;
			this.canonicalName = canonicalName;
			this.addr = addr;
			this.reachable = reachable;
			this.type = (addr.length == 4 ? "IPV4" : "IPV6");
		}
	}

	public static void main(String[] args) {
		// parse command-line arguments
		for (int i = 0; i < args.length; i++) {
			String host = args[i];
			try {
				// create InetAddress object
				InetAddress addr = InetAddress.getByName(host);
				// get the properties of InetAddress object and add it to parsing system
				addNewAddress(new Address(host, addr.getHostAddress(), addr.getCanonicalHostName(), addr.getAddress(),
						addr.isReachable(100)));
			} catch (UnknownHostException e1) {
				System.out.println("host: " + "UnknownHost");
			} catch (IOException e) {
				System.out.println("host: " + "Network Error");
			}
		}

		// print all parsed address
		for (Address addr : all) {
			System.out.println("IP Address:\t\t" + addr.ipAddr);
			System.out.println("Canonical Hostname:\t" + addr.canonicalName);
			System.out.println("Reachable:\t\t" + addr.reachable);
			System.out.println("Type:\t\t\t" + addr.type);
			System.out.println();
		}
		
		if(all.size() > 0) {
			Address addr = all.get(0);
			if(addr.highWirechy != null) {
				System.out.println("High Hierarchy:\t\t" + addr.highWirechy);
			}else {
				System.out.println("Do not have share hierarchy.");
			}
		}
	}

	/**
	 * add a new address to parsing system
	 * 
	 * @param newOne the new one address
	 */
	private static void addNewAddress(Address newOne) {
		if (newOne.addr.length == 4) {// newOne is IPV4
			// check if the newOne shares hierarchy with an existing one
			for (Address addr : all) {
				// find the same count of ip address
				int shareCount = 0;
				for (int i = 0; i < 4; i++) {
					if (newOne.addr[i] == addr.addr[i]) {
						shareCount++;
					} else {
						break;
					}
				}

				// update canonical name if share count is 3
				if (shareCount == 3) {
					addr.highWirechy = addr.ipAddr.substring(0, addr.ipAddr.lastIndexOf('.')) + ".*";
					newOne.highWirechy = addr.highWirechy;
					break;
				}

				// update canonical name if share count is 2
				if (shareCount == 2) {
					int lastDot = addr.ipAddr.lastIndexOf('.');
					addr.highWirechy = addr.ipAddr.substring(0,
							addr.ipAddr.lastIndexOf('.', lastDot - 1)) + ".*.*";
					newOne.highWirechy = addr.highWirechy;
					break;
				}
			}

			all.add(newOne);
		} else {
			// for ipv6 address, no hierarchy sharing problem
			all.add(newOne);
		}
	}

}