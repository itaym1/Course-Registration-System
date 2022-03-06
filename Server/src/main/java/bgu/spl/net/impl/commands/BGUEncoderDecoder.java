package bgu.spl.net.impl.commands;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.util.Arrays;
import java.util.LinkedList;

public class BGUEncoderDecoder implements MessageEncoderDecoder<Message> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private short currOpCode = 0; // 0 for new line

    private String userName = null;


    @Override
    public Message decodeNextByte(byte nextByte) {
        if (currOpCode == 0) { // read opCode
            short tmpOpCode = nextByte;
            if (tmpOpCode == 4) // command 4
                return new LogoutMSG();
            if (tmpOpCode == 11) // command 11
                return new MyCoursesMSG();
            currOpCode = tmpOpCode;
            return null;
        }
        if (nextByte != '\000') { // read argument
            pushByte(nextByte);
            if (currOpCode == 5 | currOpCode == 6 | currOpCode == 7 | currOpCode == 9 | currOpCode == 10) {
                if (len == 2){
                    short courseNum = popNumber();
                    short tmpOpCode = currOpCode;
                    resetArgs();
                    if (tmpOpCode == 5)
                        return new CourseRegMSG(courseNum);
                    else if (tmpOpCode == 6)
                        return new KdamCheckMSG(courseNum);
                    else if (tmpOpCode == 7)
                        return new CourseStatMSG(courseNum);
                    else if(tmpOpCode == 9)
                        return new IsRegisteredMSG(courseNum);
                    else
                        return new UnregisterMSG(courseNum);
                }
            }
        }
        else {
            // case 1: 1st argument is username
            if (userName == null && (currOpCode == 1 | currOpCode == 2 | currOpCode == 3 | currOpCode == 8) ) {
                userName = popString();
                if (currOpCode == 8) {
                    String tmp = userName;
                    resetArgs();
                    return new StudentStatMSG(tmp); // command 8
                }
                return null;
            }
            else if (userName !=null ) {
                String password = popString();
                String tmpUser = userName;
                int tmpOpCode = currOpCode;
                resetArgs();
                if (tmpOpCode == 1) // command 1
                    return new AdminRegMSG(tmpUser, password);
                else if(tmpOpCode == 2) // command 2
                    return new StudentRegMSG(tmpUser, password);
                else {// command 3
                    return new LoginMSG(tmpUser, password);
                }
            }
            else {
                pushByte(nextByte);
            }
        }
        return null;
    }

    private void resetArgs() {
        bytes = new byte[1 << 10]; //start with 1k
        len = 0;
        currOpCode = 0; // 0 for new line

        userName = null;
    }

    @Override
    public byte[] encode(Message message) {
        LinkedList<byte[]> bytesList = new LinkedList<>();
        short opCode = message.getOpCode();
        bytesList.add(shortToByteArray(opCode)); // add opCode
        bytesList.add(shortToByteArray(message.getMessageOpCode())); // add message opCode
        if (opCode == 12) {
            String msg = message.getMsg();
            if (!msg.isEmpty())
                bytesList.add(msg.getBytes()); // add optional msg
            bytesList.add(new byte[] {'\0'}); // add 0
        }

        byte[] output = {};
        for (byte[] b : bytesList) {
            output = appendByteArrays(output, b);
        }
        return output;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private String popString() {
        String result = new String(bytes, 0, len);
        len = 0;
        return result;
    }

    private short popNumber() {
        short ans = bytesToShort(bytes);
        len = 0;
        return ans;
    }

    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    private static byte[] appendByteArrays(byte[] a, byte[] b) {
        int aLength = a.length, bLength = b.length;
        byte[] output = new byte[aLength+bLength];
        for (int i = 0; i<aLength; i++) {
            output[i] = a[i];
        }
        for (int i=0; i< bLength; i++) {
            output[aLength+i] = b[i];
        }
        return output;
    }

    private static byte[] shortToByteArray(short s) {
        return new byte[] { (byte) ((s & 0xFF00) >> 8), (byte) (s & 0x00FF) };
    }
}
