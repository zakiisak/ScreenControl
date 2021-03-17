package kryoapproach;

import com.esotericsoftware.kryo.Kryo;

public class Net {
	
	public static void register(Kryo kryo)
	{
		kryo.register(byte[].class);
		kryo.register(MouseInput.class);
		kryo.register(KeyboardInput.class);
		kryo.register(MouseMove.class);
	}
	
}
