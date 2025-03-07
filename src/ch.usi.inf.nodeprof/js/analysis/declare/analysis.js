/*******************************************************************************
 * Copyright 2019 Dynamic Analysis Group, Università della Svizzera Italiana (USI)
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
//DO NOT INSTRUMENT
(function (sandbox) {
    function MyAnalysis() {
        function formatDeclareLoc(iid) {
            var locObj = J$.iidToSourceObject(iid);
            if (locObj.loc.start.line === 1 && locObj.loc.start.column === 2) {
                return '{{module}}';
            }
            return J$.iidToLocation(iid);
        }
        this.declare = function (iid, name, type) {
            console.log(`declare ${formatDeclareLoc(iid)}::${name} ${type}`);
        };
        this.read = function(iid, name, val) {
            console.log("read", J$.iidToLocation(iid), name, val);
        }
        this.write = function(iid, name, val) {
            console.log("write", J$.iidToLocation(iid), name, val);
        }
    }

    sandbox.analysis = new MyAnalysis();
})(J$);
