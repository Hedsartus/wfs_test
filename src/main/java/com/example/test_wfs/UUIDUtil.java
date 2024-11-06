package com.example.test_wfs;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.time.Clock;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Slf4j
@UtilityClass
public class UUIDUtil {

    public static String uuidToPostgres(UUID uuid) {
        if(nonNull(uuid))
            return "'" + uuid + "'::uuid";

		return null;
    }

    public static String uuidToMssql(UUID uuid) {
        if(nonNull(uuid))
            return "cast('" + uuid + "' as uniqueidentifier)";

		return null;
    }

    /** sequence UUID v 7 */
    public static UUID sqUUID ( ) {
        long nano = System.nanoTime();
        nano = nano - (nano / 1000) * 1000;

        long mostSigBits = (Clock.systemUTC().millis() << 16) | (0x7000 + Long.parseLong(""+nano, 16));

        long leastSigBits = getSrvSign();
        byte[] randomBytes = new byte[6];
        getSecureRandom().nextBytes(randomBytes);
        for ( byte b : randomBytes )
            leastSigBits = (leastSigBits << 8) | (b & 0xff);

        return new UUID(mostSigBits, leastSigBits);
    }

    /** для формирования идентификаторов при миграции */
    public static UUID mUUID ( long unixMillis, long id ) {
        long mostSigBits = (unixMillis << 16) | (0x7000);

        return new UUID(mostSigBits, id);
    }

    private SecureRandom secureRandom;

    private SecureRandom getSecureRandom ( ) {
        if ( secureRandom == null )
            secureRandom = new SecureRandom();

        return secureRandom;
    }

    private static final String SERVER_ID_SETTING_NAME = "SERVER_ID";

    /** <b>G</b>IS <b>B</b>IS <b>D</b>EFAULT <b>S</b>ERVER hex id <br><br>GBDS<br>6BD5*/
    private static final long DEFAULT_SERVER_ID = 0x6bd5;

    private static Long serverSign = null;

    /** Идентификатор сервера из четырех 16тиричный цифр из переменных окружения */
    private static long getSrvSign ( ) {
        if ( serverSign == null ) {
            Long serverId = null;
            String serverIdStr = "4000";
            if ( StringUtils.length(serverIdStr) == 4 )
                try {
                    serverId = Long.parseLong(serverIdStr, 16);
                } catch ( Exception e ) {
                    log.info("failed init hex server identifier for generate sequence UUID from setting name "+SERVER_ID_SETTING_NAME+" wiht value = {"+serverIdStr+"}");
                    e.printStackTrace();
                }
            else
                log.info("length setting value for generate sequence UUID with name "+SERVER_ID_SETTING_NAME+" has incorrect length {"+serverIdStr+"}");
            if ( serverId == null )
                serverId = DEFAULT_SERVER_ID;

            serverSign = serverId;
        }

        return serverSign;
    }

	/** Извлечение времени в Unix ms */
	public static Long uuidExtractTime(UUID uuid) {
		if ( uuid == null || uuid.version() != 7 )
			return null;

		return uuid.getMostSignificantBits() >> 16;
	}

}
