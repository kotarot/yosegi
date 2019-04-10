/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.yahoo.yosegi.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SwitchDispatcherFactory<K,V> {
  @FunctionalInterface
  public interface Func<K,V> {
    public V get(K key);
  }

  private final Map<K,V> map;
  private V defaultValue = null;

  public SwitchDispatcherFactory(Map<K,V> map) {
    this.map = map;
  }

  public SwitchDispatcherFactory() {
    this(new HashMap<K,V>());
  }

  public SwitchDispatcherFactory set(final K key, final V value) {
    map.put(key, value);
    return this;
  }

  public SwitchDispatcherFactory setDefault(final V value) {
    defaultValue = value;
    return this;
  }

  public Set<K> keySet() {
    return map.keySet();
  }

  public Func create() {
    return key -> map.getOrDefault(key, defaultValue);
  }
}
