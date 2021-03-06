package pengrui.javagl.abstraction.cores;

import java.util.Collection;

import pengrui.javagl.abstraction.basics.HasChildrenable;
import pengrui.javagl.abstraction.events.AbstractEvent;
import pengrui.javagl.abstraction.events.IEvent;
import pengrui.javagl.abstraction.events.IEventManager;
import pengrui.javagl.abstraction.util.DebugUtil;
import pengrui.javagl.abstraction.util.GlobalConfig;
import pengrui.javagl.abstraction.util.LogUtil;

/**
 * 每一个对象具备处理输入的能力
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public interface Inputable{
	
	boolean isEnableInput();
	void setEnableInput(boolean en);
	
	default void inputs(IEvent evn){
		Inputable.inputs(this, evn);
	}
	
	void onInput(IEvent evn);
	boolean isEnableChildrenInput();
	void setEnableChildrenInput(boolean en);
	/**
	 * {@link Inputable#onInput(AbstractEvent)}
	 * @param bean 
	 */
	public static void testInput(Inputable bean){//TODO 测试代码
		// 通常处理方式为 接受到外部事件 后进行内部数据变更，如果需要和其他组件交互
		// bean.innerInputProcess();
		IEvent evn = new AbstractEvent(){};
		IEventManager.handle(null,bean,bean,evn,false);
	}
	
	public static void inputs(Inputable input,IEvent evn){
		if(!GlobalConfig.INPUT_TEST_ENABLE){
			if(
					null == input 
					|| null == evn //事件处理源 或 事件为空 则不处理
					|| null==evn.getEventType()// 事件类型为空 则不处理
					|| null == evn.getDestination() //事件的目标对象为空 则不处理
					|| null == evn.getSource() // 事件的事件源为空 则不处理
					|| evn.getDestination() == evn  //事件的目标对象是源对象 则不处理 避免递归
					|| evn.getSource()!=input // 事件的事件源不是事件发生者自身 则不处理
					) {
				LogUtil.debug("input param invalid");
				return ;
			}
		}
		
		if(GlobalConfig.DEPTH_INFO_ENABLE)
			DebugUtil.depthInfo(((HasChildrenable<?>)input).getDepth(), input.getClass());
		
		if(input.isEnableInput())
			input.onInput(evn);
		
		if(!(input instanceof HasChildrenable)){
			LogUtil.info("draw not instance of the HasChildrenable ,return");
			return;
		}
		
		Collection<Inputable> children = (Collection<Inputable>)((HasChildrenable<?>)input).getChildren();
		if(null!=children && !children.isEmpty()&&input.isEnableChildrenInput()){
			for (Inputable child : children){
				if(!(child instanceof Inputable)){
					LogUtil.info("child is not instance of the Inputable , return");
					return;
				}	
				if(child.isEnableInput())
					child.inputs(evn);
			} 
		}
	}
}
