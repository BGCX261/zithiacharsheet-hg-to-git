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
package com.mcherm.zithiacharsheet.client.model;

/**
 * The enumerated type for supported races.
 * <p>
 * Design note: Perhaps the values for this can eventually be data driven. But
 * for now, I think it will be easier for me to hard-code the set of values in
 * the code.
 */
public enum Race {
    Human(),
    Dwarf(),
    Elf(),
    Hob(),
    ;

    /**
     * Private constructor.
     */
    private Race() {}
}
