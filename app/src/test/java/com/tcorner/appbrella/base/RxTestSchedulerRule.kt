package com.tcorner.appbrella.base

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Executor

class RxTestSchedulerRule : TestRule {

    val immediateScheduler: Scheduler = object : Scheduler() {
        override fun createWorker(): Scheduler.Worker {
            return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
        }
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { scheduler -> immediateScheduler }
                RxJavaPlugins.setNewThreadSchedulerHandler { scheduler -> immediateScheduler }
                RxJavaPlugins.setComputationSchedulerHandler { scheduler -> immediateScheduler }
                RxAndroidPlugins.setMainThreadSchedulerHandler { scheduler -> immediateScheduler }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediateScheduler }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}
