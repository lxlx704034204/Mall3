package com.hxqc.pay.util;//package com.hxqc.hxqcmall.pay.util;
//
//import java.lang.ref.ReferenceQueue;
//import java.lang.ref.SoftReference;
//import java.util.HashMap;
//
///**
// * 软引用的集合（需要给GC一个时间，回收对象）
// *
// * author wh
// *
// * @param <K>
// * @param <V>
// */
//public class SoftMap<K, V> extends HashMap<K, V> {
//	// 如何将V转换成软引用对象
//
//	// 定义了一个装袋子的容器
//	private HashMap<K, SoftValue<V>> temp;
//	// 定义了一个装空袋子的容器，该袋子是装V的
//	private ReferenceQueue<V> q;
//
//	public SoftMap() {
////		Object object = new Object();// 强引用对象
////		// 降低对象引用级别的方法
////		SoftReference sr = new SoftReference(object);
//
//		temp = new HashMap<K, SoftValue<V>>();
//		q=new ReferenceQueue<V>();
//
//		// 工作内容
//		// 1、将所有的手机（BaseUI子类对象），装到袋子
//		// 2、清理空袋子
//	}
//
//	@Override
//	public V put(K key, V value) {
//		// 手机装袋子中
//		SoftValue<V> sr = new SoftValue<V>(key,value,q);
//		temp.put(key, sr);
//		return null;
//	}
//
//	@Override
//	public V get(Object key) {
//		clearNullSoftReference();
//		SoftValue<V> sr = temp.get(key);
//		if (sr != null) {
//			// 从袋子获取到里面的手机 注意：垃圾回收器清除，则此方法将返回 null。（如果后面会使用到该对象的话，判断该对象是否为空）
//			return sr.get();
//		}
//
//		return null;
//	}
//
//	@Override
//	public boolean containsKey(Object key) {
//		clearNullSoftReference();
//		// 含有，首先获取到袋子，判断袋子中的手机是否为空
//		SoftValue<V> sr = temp.get(key);
//		if (sr != null) {
//			V v = sr.get();
//			if (v != null) {
//				return true;
//			}
//		}
//		return false;
//	}
//	/**
//	 * 清理空袋子
//	 */
//	private void clearNullSoftReference(){
//		// 方式一：循环==无用功
//		/*for(Map.Entry<K, SoftReference<V>> item:temp.entrySet())
//		{
//			SoftReference<V> sr = item.getValue();
//			V v = sr.get();
//			if(v==null)
//			{
//				item.getKey();
//			}
//		}
//
//		temp.remove(key)*/
//
//		// 方式二：temp那个是空袋子，谁知道temp中那个是空袋子？
//		// 空袋子怎么来？GC发现内存不足的时候，清理软引用对象，一旦清理了，就会产生空袋子
//		// 让GC做一个专业的小偷，偷东西要记账：我把“那个袋子”里面的手机个偷了
//		// 查账——为GC提供一个账本
//
//
//		// 轮询此队列，查看是否存在可用的引用对象。如果存在"一个"立即可用的对象，则从该队列中"移除"此对象并返回。否则此方法立即返回 null。
//		SoftValue<V> sr = (SoftValue<V>) q.poll();// get  remove
//		while(sr!=null)
//		{
//			System.out.println("该袋子中的手机被回收了："+sr.key);
//			temp.remove(sr.key);
//			sr =(SoftValue<V>) q.poll();
//		}
//	}
//
//	/**
//	 * 自定义的袋子：多了key的记录
//	 * author l
//	 *
//	 * @param <V>
//	 */
//	private class SoftValue<V> extends SoftReference<V>{
//		Object key;
//
//		public SoftValue(Object key,V value,ReferenceQueue<V> q) {
//			super(value, q);
//			this.key = key;
//		}
//
//
//	}
//
//}
