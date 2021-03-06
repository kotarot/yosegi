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
package jp.co.yahoo.yosegi.blackbox;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import jp.co.yahoo.yosegi.config.Configuration;

import jp.co.yahoo.yosegi.message.objects.*;

import jp.co.yahoo.yosegi.spread.expression.*;
import jp.co.yahoo.yosegi.spread.column.filter.*;
import jp.co.yahoo.yosegi.spread.column.*;
import jp.co.yahoo.yosegi.binary.*;
import jp.co.yahoo.yosegi.binary.maker.*;

public class TestBytesPrimitiveColumn {

  public static Stream<Arguments> data1() throws IOException{
    return Stream.of(
      arguments( "jp.co.yahoo.yosegi.binary.maker.DumpBytesColumnBinaryMaker" ),
      arguments( "jp.co.yahoo.yosegi.binary.maker.OptimizedNullArrayDumpBytesColumnBinaryMaker" )
    );
  }

  public IColumn createNotNullColumn( final String targetClassName ) throws IOException{
    IColumn column = new PrimitiveColumn( ColumnType.BYTES , "column" );
    column.add( ColumnType.BYTES , new BytesObj( "a".getBytes() ) , 0 );
    column.add( ColumnType.BYTES , new BytesObj( "ab".getBytes() ) , 1 );
    column.add( ColumnType.BYTES , new BytesObj( "abc".getBytes() ) , 2 );
    column.add( ColumnType.BYTES , new BytesObj( "abcd".getBytes() ) , 3 );
    column.add( ColumnType.BYTES , new BytesObj( "b".getBytes() ) , 4 );
    column.add( ColumnType.BYTES , new BytesObj( "bc".getBytes() ) , 5 );
    column.add( ColumnType.BYTES , new BytesObj( "bcd".getBytes() ) , 6 );
    column.add( ColumnType.BYTES , new BytesObj( "bcde".getBytes() ) , 7 );
    column.add( ColumnType.BYTES , new BytesObj( "c".getBytes() ) , 8 );
    column.add( ColumnType.BYTES , new BytesObj( "cd".getBytes() ) , 9 );
    column.add( ColumnType.BYTES , new BytesObj( "".getBytes() ) , 10 );

    IColumnBinaryMaker maker = FindColumnBinaryMaker.get( targetClassName );
    ColumnBinaryMakerConfig defaultConfig = new ColumnBinaryMakerConfig();
    ColumnBinaryMakerCustomConfigNode configNode = new ColumnBinaryMakerCustomConfigNode( "root" , defaultConfig );
    ColumnBinary columnBinary = maker.toBinary( defaultConfig , null , new CompressResultNode() , column );
    return FindColumnBinaryMaker.get( columnBinary.makerClassName ).toColumn( columnBinary );
  }

  public IColumn createNullColumn( final String targetClassName ) throws IOException{
    IColumn column = new PrimitiveColumn( ColumnType.BYTES , "column" );

    IColumnBinaryMaker maker = FindColumnBinaryMaker.get( targetClassName );
    ColumnBinaryMakerConfig defaultConfig = new ColumnBinaryMakerConfig();
    ColumnBinaryMakerCustomConfigNode configNode = new ColumnBinaryMakerCustomConfigNode( "root" , defaultConfig );
    ColumnBinary columnBinary = maker.toBinary( defaultConfig , null , new CompressResultNode() , column );
    return  FindColumnBinaryMaker.get( columnBinary.makerClassName ).toColumn( columnBinary );
  }

  public IColumn createHasNullColumn( final String targetClassName ) throws IOException{
    IColumn column = new PrimitiveColumn( ColumnType.BYTES , "column" );
    column.add( ColumnType.BYTES , new BytesObj( "a".getBytes() ) , 0 );
    column.add( ColumnType.BYTES , new BytesObj( "b".getBytes() ) , 4 );
    column.add( ColumnType.BYTES , new BytesObj( "c".getBytes() ) , 8 );

    IColumnBinaryMaker maker = FindColumnBinaryMaker.get( targetClassName );
    ColumnBinaryMakerConfig defaultConfig = new ColumnBinaryMakerConfig();
    ColumnBinaryMakerCustomConfigNode configNode = new ColumnBinaryMakerCustomConfigNode( "root" , defaultConfig );
    ColumnBinary columnBinary = maker.toBinary( defaultConfig , null , new CompressResultNode() , column );
    return FindColumnBinaryMaker.get( columnBinary.makerClassName ).toColumn( columnBinary );
  }

  public IColumn createLastCellColumn( final String targetClassName ) throws IOException{
    IColumn column = new PrimitiveColumn( ColumnType.BYTES , "column" );
    column.add( ColumnType.BYTES , new BytesObj( "c".getBytes() ) , 10000 );

    IColumnBinaryMaker maker = FindColumnBinaryMaker.get( targetClassName );
    ColumnBinaryMakerConfig defaultConfig = new ColumnBinaryMakerConfig();
    ColumnBinaryMakerCustomConfigNode configNode = new ColumnBinaryMakerCustomConfigNode( "root" , defaultConfig );
    ColumnBinary columnBinary = maker.toBinary( defaultConfig , null , new CompressResultNode() , column );
    return FindColumnBinaryMaker.get( columnBinary.makerClassName ).toColumn( columnBinary );
  }

  @ParameterizedTest
  @MethodSource( "data1" )
  public void T_encodeAndDecode_equalsSetValue_withNotNull( final String targetClassName ) throws IOException{
    IColumn column = createNotNullColumn( targetClassName );
    assertEquals( ( (PrimitiveObject)( column.get(0).getRow() ) ).getString() , "a" );
    assertEquals( ( (PrimitiveObject)( column.get(1).getRow() ) ).getString() , "ab" );
    assertEquals( ( (PrimitiveObject)( column.get(2).getRow() ) ).getString() , "abc" );
    assertEquals( ( (PrimitiveObject)( column.get(3).getRow() ) ).getString() , "abcd" );
    assertEquals( ( (PrimitiveObject)( column.get(4).getRow() ) ).getString() , "b" );
    assertEquals( ( (PrimitiveObject)( column.get(5).getRow() ) ).getString() , "bc" );
    assertEquals( ( (PrimitiveObject)( column.get(6).getRow() ) ).getString() , "bcd" );
    assertEquals( ( (PrimitiveObject)( column.get(7).getRow() ) ).getString() , "bcde" );
    assertEquals( ( (PrimitiveObject)( column.get(8).getRow() ) ).getString() , "c" );
    assertEquals( ( (PrimitiveObject)( column.get(9).getRow() ) ).getString() , "cd" );
    assertEquals( ( (PrimitiveObject)( column.get(10).getRow() ) ).getString() , "" );
  }

  @ParameterizedTest
  @MethodSource( "data1" )
  public void T_encodeAndDecode_equalsSetValue_withNull( final String targetClassName ) throws IOException{
    IColumn column = createNullColumn( targetClassName );
    assertNull( column.get(0).getRow() );
    assertNull( column.get(1).getRow() );
  }

  @ParameterizedTest
  @MethodSource( "data1" )
  public void T_encodeAndDecode_equalsSetValue_withHasNull( final String targetClassName ) throws IOException{
    IColumn column = createHasNullColumn( targetClassName );
    assertEquals( ( (PrimitiveObject)( column.get(0).getRow() ) ).getString() , "a" );
    assertNull( column.get(1).getRow() );
    assertNull( column.get(2).getRow() );
    assertNull( column.get(3).getRow() );
    assertEquals( ( (PrimitiveObject)( column.get(4).getRow() ) ).getString() , "b" );
    assertNull( column.get(5).getRow() );
    assertNull( column.get(6).getRow() );
    assertNull( column.get(7).getRow() );
    assertEquals( ( (PrimitiveObject)( column.get(8).getRow() ) ).getString() , "c" );
  }

  @ParameterizedTest
  @MethodSource( "data1" )
  public void T_encodeAndDecode_equalsSetValue_lastCellOnly( final String targetClassName ) throws IOException{
    IColumn column = createLastCellColumn( targetClassName );
    for( int i = 0 ; i < 10000 ; i++ ){
      assertNull( column.get(i).getRow() );
    }
    assertEquals( ( (PrimitiveObject)( column.get(10000).getRow() ) ).getString() , "c" );
  }

}
