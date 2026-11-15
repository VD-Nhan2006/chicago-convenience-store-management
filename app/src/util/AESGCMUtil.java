package util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESGCMUtil {
	private static final byte[] SECRET_KEY_BYTES = "ChicagoConvenienceStoreKey2026!!".getBytes(StandardCharsets.UTF_8);
	private static final SecretKey SECRET_KEY = new SecretKeySpec(SECRET_KEY_BYTES, "AES");
	private static final int IV_LENGTH_BYTE = 12;
	private static final int TAG_LENGTH_BIT = 128;
	
	public static String maHoaMatKhau(String matKhau) throws Exception {
		byte[] iv = new byte[IV_LENGTH_BYTE];
		new SecureRandom().nextBytes(iv);
		
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
		byte[] cipherText = cipher.doFinal(matKhau.getBytes(StandardCharsets.UTF_8));
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
		byteBuffer.put(iv);
		byteBuffer.put(cipherText);
		
		return Base64.getEncoder().encodeToString(byteBuffer.array());
	}
	
	public static String giaiMa(String base64CipherText) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(base64CipherText);
        
        ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        byteBuffer.get(iv);
        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        byte[] plainTextBytes = cipher.doFinal(cipherText);
        return new String(plainTextBytes, StandardCharsets.UTF_8);
    }
}
