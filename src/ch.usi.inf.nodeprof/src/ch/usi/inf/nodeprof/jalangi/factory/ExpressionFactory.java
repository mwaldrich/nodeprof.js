/*******************************************************************************
 * Copyright 2018 Dynamic Analysis Group, Università della Svizzera Italiana (USI)
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

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.object.DynamicObject;

import ch.usi.inf.nodeprof.handlers.BaseEventHandlerNode;
import ch.usi.inf.nodeprof.handlers.ExpressionEventHandler;

public class ExpressionFactory extends AbstractFactory {

    @TruffleBoundary
    public ExpressionFactory(Object jalangiAnalysis, DynamicObject pre, DynamicObject post) {
        super("expression", jalangiAnalysis, pre, post, 2, 3);
    }

    @Override
    public BaseEventHandlerNode create(EventContext context) {
        return new ExpressionEventHandler(context) {

            @Child DirectCallNode preCall = createPreCallNode();
            @Child DirectCallNode postCall = createPostCallNode();

            @Override
            public void executePre(VirtualFrame frame, Object[] inputs) {
                if (pre != null) {
                    setPreArguments(0, this.getSourceIID());
                    setPreArguments(1, this.getExpressionType());
                    directCall(preCall, true, getSourceIID());
                }
            }

            @Override
            public void executePost(VirtualFrame frame, Object result,
                            Object[] inputs) {
                if (post != null) {
                    setPostArguments(0, this.getSourceIID());
                    setPostArguments(1, this.getExpressionType());
                    setPostArguments(2, result);
                    directCall(postCall, false, getSourceIID());
                }
            }

        };
    }

}
