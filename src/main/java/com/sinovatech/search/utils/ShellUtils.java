package com.sinovatech.search.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ShellUtils {
	private static final Logger log = LoggerFactory.getLogger(ShellUtils.class);
	private static JSch jsch;
	private static Session session;

	/**
	 * 连接到指定的IP
	 * 
	 * @throws JSchException
	 */
	public static void connect(String user, String passwd, String host,int port) throws JSchException {
		jsch = new JSch();
		session = jsch.getSession(user, host, port);
		session.setPassword(passwd);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
	}

	/**
	 * 执行相关的命令
	 * 
	 * @throws JSchException
	 */
	public static void execCmd(String command, String user, String passwd, String host,int port) throws JSchException {
		connect(user, passwd, host,port);

		BufferedReader reader = null;
		Channel channel = null;
		InputStream in = null;
		try {
			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);
			channel.connect();
			in = channel.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in));
			String buf = null;
			while ((buf = reader.readLine()) != null) {
				log.info(buf);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != in) {
					in.close();
				}
				if (null != reader) {
					reader.close();
				}
				if (null != channel) {
					channel.disconnect();
				}
				if (null != session) {
					session.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		String remoteDir = "/app/weblogic/Oracle/Middleware/user_projects/domains/domain_116114/ticketftp/data";
		String fileName = "/116114ticket2013100714-bak20131012110206.xls";
		try {
			// execCmd("ls", "weblogic", "yqMj8bvibm", "10.143.131.53");
			execCmd("mv " + remoteDir + fileName + " " + remoteDir + "/bak", "weblogic", "yqMj8bvibm", "10.143.131.53",22);
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}