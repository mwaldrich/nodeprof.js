/*******************************************************************************
 * Copyright 2018 Dynamic Analysis Group, Università della Svizzera Italiana (USI)
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package ch.usi.inf.nodeprof.jalangi.factory;

import ch.usi.inf.nodeprof.utils.Logger;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.TruffleInstrument;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.js.nodes.control.ReturnException;
import com.oracle.truffle.js.runtime.objects.Undefined;

import ch.usi.inf.nodeprof.handlers.BaseEventHandlerNode;
import ch.usi.inf.nodeprof.handlers.FunctionRootEventHandler;

public class RootFactory extends AbstractFactory {

    protected final TruffleInstrument.Env env;

    public RootFactory(Object jalangiAnalysis, DynamicObject pre, DynamicObject post, TruffleInstrument.Env env) {
        super("function", jalangiAnalysis, pre, post, 4, 3);
        this.env = env;
    }

    @Override
    public BaseEventHandlerNode create(EventContext context) {
        return new FunctionRootEventHandler(context) {
            @Child MakeArgumentArrayNode makeArgs = MakeArgumentArrayNodeGen.create(pre == null ? post : pre, 2, 0);
            @Child DirectCallNode preCall = createDirectCallNode(pre);
            @Child DirectCallNode postCall = createDirectCallNode(post);

            @Override
            public void executePre(VirtualFrame frame, Object[] inputs) {
                if (isRegularExpression()) {
                    return;
                }

                if (!this.isBuiltin && pre != null) {

                    setPreArguments(0, getSourceIID());
                    setPreArguments(1, getFunction(frame));
                    setPreArguments(2, getReceiver(frame, env));
                    setPreArguments(3, makeArgs.executeArguments(getArguments(frame)));

                    directCall(preCall, true, getSourceIID());
                }
            }

            @Override
            public void executePost(VirtualFrame frame, Object result,
                            Object[] inputs) {
                if (isRegularExpression()) {
                    return;
                }

                if (!this.isBuiltin && post != null) {
                    setPostArguments(0, this.getSourceIID());
                    setPostArguments(1, convertResult(result));
                    setPostArguments(2, createWrappedException(null));
                    directCall(postCall, false, getSourceIID());
                }
            }

            @Override
            public void executeExceptional(VirtualFrame frame, Throwable exception) {
                if (isRegularExpression()) {
                    return;
                }



                if (!this.isBuiltin && post != null) {
                    setPostArguments(0, getSourceIID());
                    setPostArguments(1, Undefined.instance);
                    setPostArguments(2, createWrappedException(exception));
                    directCall(postCall, false, getSourceIID());
                }
            }

            @Override
            public void executeExceptionalCtrlFlow(VirtualFrame frame, Throwable exception,
                                                   Object[] inputs) {
                // ignore Truffle-internal control flow exceptions
                if (exception instanceof ReturnException) {
                    executePost(frame, ((ReturnException) exception).getResult(), inputs);
                    return;
                }

                Logger.error("Unexpected control flow exception: " + exception.getClass().getSimpleName());
                executeExceptional(frame, exception);
            }
        };
    }
}
