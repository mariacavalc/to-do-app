package com.madu.to_doapp

fun tasksFilter(tasks: List<Task>): List<Task>{
    return tasks.filter { !it.done }
}