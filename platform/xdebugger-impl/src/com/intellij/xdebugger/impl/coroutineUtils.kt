// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.xdebugger.impl

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.impl.editorId
import com.intellij.openapi.project.Project
import com.intellij.platform.project.projectId
import com.intellij.xdebugger.impl.frame.XDebugManagerProxy
import com.intellij.xdebugger.impl.rpc.XDebuggerManagerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Used only for Java code, since MutableStateFlow function cannot be called there.
internal fun <T> createMutableStateFlow(initialValue: T): MutableStateFlow<T> {
  return MutableStateFlow(initialValue)
}

// Used only for Java code, since MutableSharedFlow function cannot be called there.
internal fun <T> createMutableSharedFlow(replay: Int, extraBufferCapacity: Int): MutableSharedFlow<T> {
  return MutableSharedFlow(replay, extraBufferCapacity)
}

@Service(Service.Level.PROJECT)
internal class XDebugSessionSelectionService(project: Project, scope: CoroutineScope) {
  init {
    scope.launch {
      XDebugManagerProxy.getInstance().getCurrentSessionFlow(project).collectLatest { currentSession ->
        // switch to EDT, so select can execute immediately (it uses invokeLaterIfNeeded)
        withContext(Dispatchers.EDT) {
          currentSession?.sessionTab?.select()
        }
      }
    }
  }

  companion object {
    @JvmStatic
    fun startCurrentSessionListening(project: Project) {
      project.service<XDebugSessionSelectionService>()
    }
  }
}

internal fun performDebuggerActionAsync(e: AnActionEvent, action: suspend () -> Unit) {
  performDebuggerActionAsync(e.project, e.dataContext, action)
}

internal fun performDebuggerActionAsync(
  project: Project?,
  dataContext: DataContext,
  action: suspend () -> Unit,
) {
  val coroutineScope = project?.service<FrontendDebuggerActionProjectCoroutineScope>()?.cs
                       ?: service<FrontendDebuggerActionCoroutineScope>().cs

  coroutineScope.launch {
    action()
    val editor = dataContext.getData(CommonDataKeys.EDITOR)
    if (project != null && editor != null) {
      withContext(Dispatchers.EDT) {
        XDebuggerManagerApi.getInstance().reshowInlays(project.projectId(), editor.editorId())
      }
    }
  }
}

@Service(Service.Level.APP)
private class FrontendDebuggerActionCoroutineScope(val cs: CoroutineScope)

@Service(Service.Level.PROJECT)
private class FrontendDebuggerActionProjectCoroutineScope(val project: Project, val cs: CoroutineScope)