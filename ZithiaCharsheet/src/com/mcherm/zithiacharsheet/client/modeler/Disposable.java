/*
 * Copyright 2009 Michael Chermside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcherm.zithiacharsheet.client.modeler;

/**
 * An interface for things which need to be cleaned up before they get
 * garbage-collected.
 */
public interface Disposable {

    /**
     * This should be called before the object is ready to be garbage collected.
     * It gives the object an opportunity to clean up things (such as references
     * which might keep it alive). Do not continue to use the object after
     * calling this.
     */
    public void dispose();
}
