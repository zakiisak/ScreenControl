package kryoapproach;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class KryoServer extends Server {
	
	private Listener listener = new Listener()
	{
		public void received(Connection c, Object data) { KryoServer.this.received(c, data);}
		public void connected(Connection c) { KryoServer.this.connected(c); }
		public void disconnected(Connection c) { KryoServer.this.disconnected(c);}
	};
	
	protected void registerClasses()
	{
		
	}
	
	public KryoServer(int writeBufferSize, int objectBufferSize, int port) throws IOException
	{
		super(writeBufferSize, objectBufferSize);
		addListener(listener);
		start();
		bind(port, port);
		registerClasses();
	}

	protected void connected(Connection c) {
		
	}

	protected void disconnected(Connection c) {
		
	}

	protected void received(Connection c, Object data) {
		
	}
	
	
	
}
