package com.hc77.utils;

/*
 * 接口INewObjectObserver
 * 用途：DataImporter在检测到有新文件、解析出对应Object后，使用本接口通知应用方有新数据到达
 * 模板参数T：应用要使用的Class
 */
public interface INewObjectObserver<T> {
	public boolean onNewObjectArrived(T obj);
}
