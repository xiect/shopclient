package com.brains.framework.task;

import android.os.AsyncTask;

/**
 * task基础类
 * @author xiect
 *
 */
public abstract class GenericTask extends
        AsyncTask<TaskParams, Object, TaskResult> {
 
    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
    
    abstract protected TaskResult _doInBackground(TaskParams... params);
    
    @Override
    protected void onPostExecute(TaskResult result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected TaskResult doInBackground(TaskParams... params) {
    	TaskResult result = _doInBackground(params);
        return result;
    }

}
