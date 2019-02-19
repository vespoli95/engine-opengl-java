package pengrui.javagl.abstraction.managers;

import java.util.Collection;

import org.lwjgl.opengl.GL11;

import pengrui.javagl.abstraction.basics.HasChildrenable;
import pengrui.javagl.abstraction.cores.Drawable;
import pengrui.javagl.abstraction.util.LogUtil;

public interface IDrawableManager extends Manageable<Drawable>{
	@Override
	void register(Drawable bean);// by Manageable
	@Override
	void unregister(Drawable bean);// by Manageable
	@Override
	Collection<Drawable> getAll();// by Manageable
	@Override
	void init(); // by Lifecyclable
	@Override
	void destroy();// by Lifecyclable
	void draws();
	
	public static void register(IDrawableManager dm,Drawable bean) {
		if(HasChildrenable.isOneLevelBean(bean)){
			Manageable.register(dm, bean);
		}else{
			LogUtil.debug("is not one level object, ignore register");
		}
	}
	
	public static void unregister(IDrawableManager dm,Drawable bean) {
		if(HasChildrenable.isOneLevelBean(bean)){
			Manageable.unregister(dm, bean);
		}else{
			LogUtil.debug("is not one level object, ignore remove");
		}
	}
	
	public static void destroy(IDrawableManager am){
		Manageable.destroy(am);
	}

	public static void draws(IDrawableManager dm) {
		Manageable.checkManager(dm);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
				|GL11.GL_DEPTH_BUFFER_BIT
				);
		
		for (Drawable drawable : dm.getAll()) {
				drawable.draws();
		}
	}
	
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
}
