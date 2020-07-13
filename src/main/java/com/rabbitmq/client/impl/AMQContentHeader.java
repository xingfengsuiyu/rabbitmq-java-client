// Copyright (c) 2007-2020 VMware, Inc. or its affiliates.  All rights reserved.
//
// This software, the RabbitMQ Java client library, is triple-licensed under the
// Mozilla Public License 2.0 ("MPL"), the GNU General Public License version 2
// ("GPL") and the Apache License version 2 ("ASL"). For the MPL, please see
// LICENSE-MPL-RabbitMQ. For the GPL, please see LICENSE-GPL2.  For the ASL,
// please see LICENSE-APACHE2.
//
// This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND,
// either express or implied. See the LICENSE file for specific language governing
// rights and limitations of this software.
//
// If you have any questions regarding licensing, please contact us at
// info@rabbitmq.com.

package com.rabbitmq.client.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ContentHeader;

/**
 * Implementation of ContentHeader - specialized by autogenerated code in AMQP.java.
 */

public abstract class AMQContentHeader implements ContentHeader {
    /**
     * Private API - Called by {@link AMQChannel#handleFrame}. Parses the header frame.
     */
    private long bodySize; 
    
    protected AMQContentHeader() {
        this.bodySize = 0;
    }
    
    protected AMQContentHeader(DataInputStream in) throws IOException {
        in.readShort(); // weight not currently used
        this.bodySize = in.readLong();
    }
    
    public long getBodySize() { return bodySize; }
    

    private void writeTo(DataOutputStream out, long bodySize) throws IOException {
        out.writeShort(0); // weight - not currently used
        out.writeLong(bodySize);
        writePropertiesTo(new ContentHeaderPropertyWriter(out));
    }

    /**
     * Private API - Autogenerated writer for this header
     */
    public abstract void writePropertiesTo(ContentHeaderPropertyWriter writer) throws IOException;

    /** Public API - {@inheritDoc} */
    @Override
    public void appendPropertyDebugStringTo(StringBuilder acc) {
        acc.append("(?)");
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#contentHeader<").append(getClassName()).append(">");
        this.appendPropertyDebugStringTo(sb);
        return sb.toString();
    }

    /**
     * Private API - Called by {@link AMQCommand#transmit}
     */
    public Frame toFrame(int channelNumber, long bodySize) throws IOException {
        Frame frame = new Frame(AMQP.FRAME_HEADER, channelNumber);
        DataOutputStream bodyOut = frame.getOutputStream();
        bodyOut.writeShort(getClassId());
        writeTo(bodyOut, bodySize);
        return frame;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
