package pengrui.javagl.abstraction.factorys;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import pengrui.javagl.abstraction.basics.Factoryable;
import pengrui.javagl.abstraction.cores.Inputable;
import pengrui.javagl.abstraction.events.AbstractEvent;
import pengrui.javagl.abstraction.events.IEvent;
import pengrui.javagl.abstraction.managers.IInputableManager;
import pengrui.javagl.abstraction.managers.Manageable;
import pengrui.javagl.manager.AbstractInputManager;

public interface InputManagerFactory extends Factoryable<IInputableManager>{
	
	public IInputableManager instance();

	static class Impl extends AbstractInputManager{
		Set<Inputable> beans = new HashSet<Inputable>();
		@Override
		public void init() {
			//nothing
		}
		
		@Override
		public Collection<Inputable> getAll() {
			return beans;
		}
		
		@Override
		public void destroy() {
			Manageable.destroy(this);
			beans = null; // help GC
		}
		
		@Override
		public void inputs() {
			// TODO Auto-generated method stub
			//捕获事件 然后分发事件
			IEvent evn =new AbstractEvent();
			evn.setSource(new Object());
			for(Inputable i:getAll()){
				i.inputs(evn);
			}
		}

	}
	
	static final IInputableManager INSTANCE = new Impl();
	 
	public static IInputableManager getInstance(){
		return INSTANCE;
	}
}
