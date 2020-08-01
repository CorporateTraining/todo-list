package com.thoughtworks.todo_list.ui.task;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.thoughtworks.todo_list.ui.task.model.TaskModel;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TaskViewModel extends ViewModel {
    private MutableLiveData<List<TaskModel>> tasksMutableLiveData = new MutableLiveData<>();
    private TaskRepository taskRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final static String TAG = "LoginViewModel";

    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    void observeTasks(LifecycleOwner lifecycleOwner, Observer<List<TaskModel>> observer) {
        tasksMutableLiveData.observe(lifecycleOwner, observer);
    }

    public void getTasks() {
        Single<List<TaskModel>> tasks = taskRepository.findTasks();
        tasks.map(taskModels -> {
            Comparator<TaskModel> reversedChecked = ((Comparator<TaskModel>) (taskModel1, taskModel2) -> {
                Boolean checked1 = taskModel1.getChecked();
                Boolean checked2 = taskModel2.getChecked();
                if (checked1 ^ checked2) {
                    return checked1 ? -1 : 1;
                } else {
                    return 0;
                }
            }).reversed();
            Comparator<TaskModel> reversedData = Comparator.comparing(TaskModel::getDate);
            return taskModels.stream().sorted(reversedChecked.thenComparing(reversedData))
                    .collect(Collectors.toList()); })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<TaskModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: getTasks");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<TaskModel> tasks) {
                        Log.d(TAG, "onSuccess: getTasks");
                        tasksMutableLiveData.postValue(tasks);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: getTasks", e);
                    }
                });
    }

    public void updateTask(TaskModel taskRequest){
        Completable completable = taskRepository.updateTask(taskRequest);
        completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: save");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onSuccess: save");
                        getTasks();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: save", e);
                    }
                });
    }

    public void saveTask(TaskModel taskModel){
        Completable completable = taskRepository.save(taskModel);
        completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: save");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onSuccess: save");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: save", e);
                    }
                });
    }
}
