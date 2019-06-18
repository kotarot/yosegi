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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TestIndexAndObject {

  @Test
  public void createNewInstance(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 0 );
  }

  @Test
  public void createNewInstanceWithValueLessThanZero(){
    assertThrows( RuntimeException.class ,
      () -> {
        IndexAndObject<String> indexAndObj = new IndexAndObject<String>( -1 );
      }
    );
  }

  @Test
  public void getStartIndexWithZero(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 0 );
    assertEquals( indexAndObj.getStartIndex() , 0 );
  }

  @Test
  public void getStartIndexWithMoreThanZero(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 100 );
    assertEquals( indexAndObj.getStartIndex() , 100 );
  }

  @Test
  public void addAndGetOneObjectWithIndexIsZero(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 0 );
    indexAndObj.add( "test0" );
    assertEquals( indexAndObj.get(0) , "test0" );
  }

  @Test
  public void addAndGetSameObjectsWithIndexIsZero(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 0 );
    indexAndObj.add( "test0" );
    indexAndObj.add( "test1" );
    assertEquals( indexAndObj.get(0) , "test0" );
    assertEquals( indexAndObj.get(1) , "test1" );
  }

  @Test
  public void addAndGetOneObjectWithIndexMoreThanZero(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 5 );
    indexAndObj.add( "test0" );
    assertEquals( indexAndObj.get(5) , "test0" );
  }

  @Test
  public void addAndGetSameObjectsWithIndexMoreThanZero(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 5 );
    indexAndObj.add( "test0" );
    indexAndObj.add( "test1" );
    assertEquals( indexAndObj.get(5) , "test0" );
    assertEquals( indexAndObj.get(6) , "test1" );
  }

  @Test
  public void getIndexSmallerThanStartIndex(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 5 );
    indexAndObj.add( "test0" );
    assertThrows( ArrayIndexOutOfBoundsException.class ,
      () -> {
        indexAndObj.get(4);
      }
    );
  }

  @Test
  public void getIndexLargerThanStartIndex(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 5 );
    indexAndObj.add( "test0" );
    assertThrows( IndexOutOfBoundsException.class ,
      () -> {
        indexAndObj.get(6);
      }
    );
  }

  @Test
  public void sizeWithIndexIsZero(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 0 );
    assertEquals( indexAndObj.size() , 0 );
    indexAndObj.add( "test0" );
    assertEquals( indexAndObj.size() , 1 );
    indexAndObj.add( "test1" );
    assertEquals( indexAndObj.size() , 2 );
  }

  @Test
  public void sizeWithIndexMoreThanZero(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 5 );
    assertEquals( indexAndObj.size() , 0 );
    indexAndObj.add( "test0" );
    assertEquals( indexAndObj.size() , 1 );
    indexAndObj.add( "test1" );
    assertEquals( indexAndObj.size() , 2 );
  }

  @Test
  public void getNextIndexWithIndexIsZero(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 0 );
    assertEquals( indexAndObj.getNextIndex() , 0 );
    indexAndObj.add( "test0" );
    assertEquals( indexAndObj.getNextIndex() , 1 );
    indexAndObj.add( "test1" );
    assertEquals( indexAndObj.getNextIndex() , 2 );
  }

  @Test
  public void getNextIndexWithIndexMoreThanZero(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 5 );
    assertEquals( indexAndObj.getNextIndex() , 5 );
    indexAndObj.add( "test0" );
    assertEquals( indexAndObj.getNextIndex() , 6 );
    indexAndObj.add( "test1" );
    assertEquals( indexAndObj.getNextIndex() , 7 );
  }

  @Test
  public void hasIndexIndexWithIndexIsZero(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 0 );
    indexAndObj.add( "test0" );
    indexAndObj.add( "test1" );
    assertEquals( indexAndObj.hasIndex( -1 ) , -1 );
    assertEquals( indexAndObj.hasIndex( 0 ) , 0 );
    assertEquals( indexAndObj.hasIndex( 1 ) , 0 );
    assertEquals( indexAndObj.hasIndex( 2 ) , 1 );
  }

  @Test
  public void hasIndexWithIndexMoreThanZero(){
    IndexAndObject<String> indexAndObj = new IndexAndObject<String>( 5 );
    indexAndObj.add( "test0" );
    indexAndObj.add( "test1" );
    assertEquals( indexAndObj.hasIndex( 4 ) , -1 );
    assertEquals( indexAndObj.hasIndex( 5 ) , 0 );
    assertEquals( indexAndObj.hasIndex( 6 ) , 0 );
    assertEquals( indexAndObj.hasIndex( 7 ) , 1 );
  }

}
