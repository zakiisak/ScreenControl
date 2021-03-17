package kryoapproach;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class KryoClient extends Client {
	
	private Listener listener = new Listener()
	{
		public void received(Connection c, Object data) { KryoClient.this.received(c, data);}
		public void connected(Connection c) { KryoClient.this.connected(c); }
		public void disconnected(Connection c) { KryoClient.this.disconnected(c);}
	};
	
	protected void registerClasses()
	{
		
	}
	
	public KryoClient(int writeBufferSize, int objectBufferSize, String host, int port) throws IOException
	{
		super(writeBufferSize, objectBufferSize);
		addListener(listener);
		start();
		connect(3000, host, port, port);
		registerClasses();
	}

	protected void connected(Connection c) {
		
	}

	protected void disconnected(Connection c) {
		
	}

	protected void received(Connection c, Object data) {
		
	}
	
	
	
}
