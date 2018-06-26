package com.rhtech.newstack;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.ServerConnection;
import io.undertow.server.protocol.http.HttpServerConnection;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;
import io.undertow.util.Protocols;

import java.io.IOException;
import java.nio.ByteBuffer;

import lombok.SneakyThrows;

import org.xnio.OptionMap;
import org.xnio.StreamConnection;
import org.xnio.XnioIoThread;
import org.xnio.conduits.ConduitStreamSinkChannel;
import org.xnio.conduits.ConduitStreamSourceChannel;
import org.xnio.conduits.StreamSinkConduit;
import org.xnio.conduits.StreamSourceConduit;

public class HttpServerExchangeStub {

    public static HttpServerExchange createHttpExchange() {
        final HeaderMap headerMap = new HeaderMap();
        final StreamConnection streamConnection = createStreamConnection();
        final OptionMap options = OptionMap.EMPTY;
        final ServerConnection connection = new HttpServerConnection( streamConnection, null, null, options, 0, null );
        return createHttpExchange( connection, headerMap );
    }

    @SneakyThrows
    private static StreamConnection createStreamConnection() {
        final StreamConnection streamConnection = mock( StreamConnection.class );
        ConduitStreamSinkChannel sinkChannel = createSinkChannel();
        when( streamConnection.getSinkChannel() ).thenReturn( sinkChannel );
        ConduitStreamSourceChannel sourceChannel = createSourceChannel();
        when( streamConnection.getSourceChannel() ).thenReturn( sourceChannel );
        XnioIoThread ioThread = mock( XnioIoThread.class );
        when( streamConnection.getIoThread() ).thenReturn( ioThread );
        return streamConnection;
    }

    private static ConduitStreamSinkChannel createSinkChannel() throws IOException {
        StreamSinkConduit sinkConduit = mock( StreamSinkConduit.class );
        when( sinkConduit.write( any( ByteBuffer.class ) ) ).thenReturn( 1 );
        ConduitStreamSinkChannel sinkChannel = new ConduitStreamSinkChannel( null, sinkConduit );
        return sinkChannel;
    }

    private static ConduitStreamSourceChannel createSourceChannel() {
        StreamSourceConduit sourceConduit = mock( StreamSourceConduit.class );
        ConduitStreamSourceChannel sourceChannel = new ConduitStreamSourceChannel( null, sourceConduit );
        return sourceChannel;
    }

    private static HttpServerExchange createHttpExchange( ServerConnection connection, HeaderMap headerMap ) {
        HttpServerExchange httpServerExchange = new HttpServerExchange( connection, null, headerMap, 200 );
        httpServerExchange.setRequestMethod( new HttpString( "GET" ) );
        httpServerExchange.setProtocol( Protocols.HTTP_1_1 );
        return httpServerExchange;
    }
}
