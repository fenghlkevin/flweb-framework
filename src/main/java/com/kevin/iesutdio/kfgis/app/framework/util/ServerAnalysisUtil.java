package com.kevin.iesutdio.kfgis.app.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * server能力分析进程
 * 
 * @author fengheliang
 * 
 */
public class ServerAnalysisUtil {
	
	private static Logger logger=LoggerFactory.getLogger(ServerAnalysisUtil.class);

	public static class ServerConfigure {

		private String ip;

		private String hostname;;

		private Long totalMemory;

		private Integer cpuCount;

		private Integer cpuMHz = 3000;

		private double usage = 0.7;

		public Long getTotalMemory() {
			return totalMemory;
		}

		public void setTotalMemory(Long totalMemory) {
			this.totalMemory = totalMemory;
		}

		public Integer getCpuCount() {
			return cpuCount;
		}

		public void setCpuCount(Integer cpuCount) {
			this.cpuCount = cpuCount;
		}

		public double getUsage() {
			return usage;
		}

		public void setUsage(double usage) {
			this.usage = usage;
		}

		public Integer getCpuMHz() {
			return cpuMHz;
		}

		public void setCpuMHz(Integer cpuMHz) {
			this.cpuMHz = cpuMHz;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getHostname() {
			return hostname;
		}

		public void setHostname(String hostname) {
			this.hostname = hostname;
		}

		public String getServerInfo() {
			return ip + "##" + hostname + "##" + totalMemory + "##" + cpuCount + "##" + cpuMHz + "##" + usage;
		}

		@Override
		public String toString() {
			return "ServerConfigure [ip=" + ip + ", hostname=" + hostname + ", totalMemory=" + totalMemory + ", cpuCount=" + cpuCount + ", cpuMHz=" + cpuMHz + ", usage=" + usage
					+ "]";
		}
	}

	public static ServerConfigure execute() {
		ServerConfigure sc = new ServerConfigure();
		Long jvm = Runtime.getRuntime().freeMemory() / 1000000L;
		sc.setTotalMemory(jvm);
		if ("linux".equalsIgnoreCase(System.getProperties().getProperty("os.name"))) {
			try {
				String[] cpus=getLinuxCpu();
				sc.setCpuCount(new Integer(cpus[0]));
				sc.setCpuMHz(new Integer(cpus[1]));
			} catch (IOException e) {
				logger.error("Get Server CPU Information Error.",e);
			}
		}
		
		try {
			InetAddress addr = InetAddress.getLocalHost();
			sc.setIp(addr.getHostAddress());
			sc.setHostname(addr.getHostName());
		} catch (UnknownHostException e) {
			logger.error("Get Server IP Information Error.",e);
		}
		return sc;
	}

	private static String[] getLinuxCpu() throws IOException {
		String commands = "lscpu";
		Process process = Runtime.getRuntime().exec(commands);

		InputStreamReader ir = new InputStreamReader(process.getInputStream());

		BufferedReader input = new BufferedReader(ir);

		String line;

		String cpus = "0";
		String cpumhz = "0";
		while ((line = input.readLine()) != null) {
			if (line.startsWith("CPU(s)")) {
				cpus = getNumber(line);
			}

			if (line.indexOf("CPU MHz") > 0) {
				cpumhz = getNumber(line);
			}
		}
		return new String[] { cpus, cpumhz };
	}

	private static String getNumber(String args) {
		String reValue = "";
		for (int i = 0; i < args.length(); i++) {
			if (Character.isDigit(args.charAt(i)) || args.charAt(i) == '.' || args.charAt(i) == '-') {
				reValue += args.charAt(i);
			}
		}
		return reValue;
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("===========os.name:" + System.getProperties().getProperty("os.name"));
		System.out.println("===========file.separator:" + System.getProperties().getProperty("file.separator"));
		ServerAnalysisUtil sat = new ServerAnalysisUtil();

		String commands = "lscpu";

		Process process = Runtime.getRuntime().exec(commands);

		InputStreamReader ir = new InputStreamReader(process.getInputStream());

		BufferedReader input = new BufferedReader(ir);

		String line;

		while ((line = input.readLine()) != null) {
			System.out.println(line);
		}

	}

}
