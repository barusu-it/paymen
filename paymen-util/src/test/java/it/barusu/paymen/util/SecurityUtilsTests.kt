package it.barusu.paymen.util

import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.Hex
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.nio.charset.Charset
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.PBEParameterSpec

class SecurityUtilsTests {
    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(this::class.java)

        const val BASE64_PRIVATE_KEY = "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"
        const val BASE64_PUBLIC_KEY = "MIIDfzCCAmegAwIBAgIERTV1ijANBgkqhkiG9w0BAQsFADBwMQswCQYDVQQGEwI4NjERMA8GA1UECBMIU2hhbmdoYWkxETAPBgNVBAcTCFNoYW5naGFpMREwDwYDVQQKEwhEaWFucm9uZzEQMA4GA1UECxMHRmluQ29yZTEWMBQGA1UEAxMNRGlhbnJvbmcgU3R1YjAeFw0xNTAzMTYwMzAyMjlaFw0xNTA2MTQwMzAyMjlaMHAxCzAJBgNVBAYTAjg2MREwDwYDVQQIEwhTaGFuZ2hhaTERMA8GA1UEBxMIU2hhbmdoYWkxETAPBgNVBAoTCERpYW5yb25nMRAwDgYDVQQLEwdGaW5Db3JlMRYwFAYDVQQDEw1EaWFucm9uZyBTdHViMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhwGDWqfbmdjg9/xkzqyXEKdAxTBQFDyFMbFEg9f5gPZneMqDaLOBiVm8bL5mOUNWd8qy/x3TFJlthuk2XGkoX85rif7Dz3H3NPpIQV2x/m7BA22j6dhYXQcu7iPVEdTlhrcafyxWa9KFVTz6aom71foS6W9RaQim4ZTnYD8OzCdiIDC1xOTVDkJso9s6MUNZbiE7xXTyUcre452pCJj+Cui0yp5vnm4PSkZI6zQyOnJVvP7/IEV1rlbFry6usbjPZi9hCXj0f8kfBYgr+/bOnK4I4Ya5nwMZ8ocBMRpaFM7IphLiRw44VUaf2STwEpyeioQ5C34K99p0f/4FsUBhaQIDAQABoyEwHzAdBgNVHQ4EFgQU7/+jPxEEiUl3+Fc+56oqdB4bxbkwDQYJKoZIhvcNAQELBQADggEBAAlt0dnCArwRUQECMphsVhsx1DyTXNBWyKYfo7zW3GwybdPVfSBn3kw3L2uqTlvQHH/B0+D1zarpG6CQBBQX3Xajk/inU+N7WTvAQwdZASWdhiSKiLj7eFAEBjeJ0aak4/d1ZPHQGHOKa7AtzeZy/3exzQczkHHV3mgddUkVYwgsRfuSi9yzX13o4cW8U2NhKb45YUG9/SPKiPtbWbct/xGD3PTu7XDmB9/D3s1qewYuD6Qs3e4ojpnCgOJn0jzH86J89I38WJEIVyuZgYAKCxO+49M8XBuj6PhAvxGRp7vAl5bhrnEqAiVSj59y92q/eNm0EMQ8t0DOaXaeKIv6Qxg="
    }

    @DisplayName("test digest")
    @Test
    fun testDigest() {
        val data = "123"
        val bytes = SecurityUtils.digest(SecurityUtils.MD5, data.toByteArray())
        val digested = Hex.encodeHexString(bytes)
        log.info("digested string: $digested")
        assertThat(digested)
                .isNotNull()
                .isEqualTo("202cb962ac59075b964b07152d234b70")
    }

    @DisplayName("test private key")
    @Test
    fun testPrivateKey() {
        val content = BASE64_PRIVATE_KEY
        val password = "123456"
        val privateKey = SecurityUtils.getPrivateKey(SecurityUtils.PKCS12, content, password)

        assertThat(privateKey).isNotNull
        assertThat(privateKey.algorithm).isEqualTo(SecurityUtils.RSA)
        assertThat(privateKey.encoded).hasSizeGreaterThan(0)
    }

    @DisplayName("test public key")
    @Test
    fun testPublicKey() {
        val content = BASE64_PUBLIC_KEY
        val publicKey = SecurityUtils.getPublicKey(SecurityUtils.X509, content)

        assertThat(publicKey).isNotNull
        assertThat(publicKey.algorithm).isEqualTo(SecurityUtils.RSA)
        assertThat(publicKey.encoded).hasSizeGreaterThan(0)
    }

    @DisplayName("test salt")
    @Test
    fun testSalt() {
        val salt = SecurityUtils.generateSalt(SecurityUtils.SHA1PRNG)

        assertThat(salt).isNotNull()
        assertThat(salt).hasSize(8)
        assertThat(Hex.encodeHexString(salt)).hasSize(16)
    }

    @DisplayName("test secret key")
    @Test
    fun testSecretKey() {
        val key = SecurityUtils.getSecretKey(SecurityUtils.DESEDE)
        assertThat(key).isNotNull
        assertThat(key.algorithm).isEqualTo(SecurityUtils.DESEDE)
        assertThat(key.format).isEqualTo("RAW")
        assertThat(key.encoded).hasSize(24)
    }

    @DisplayName("test secret key with spec")
    @Test
    fun testSecretKeyWithSpec() {
        val password = "123456"
        val spec = PBEKeySpec(password.toCharArray())
        val key = SecurityUtils.getSecretKey(SecurityUtils.PBE_WITH_SHA256_AND_256BITAES_CBC_BC, spec)

        assertThat(key).isNotNull
        assertThat(key.algorithm).isEqualTo(SecurityUtils.PBE_WITH_SHA256_AND_256BITAES_CBC_BC)
        assertThat(key.format).isEqualTo("RAW")
        assertThat(key.encoded).hasSize(password.toCharArray().size * 2 + 2)
    }


    @DisplayName("test sign and verify")
    @Test
    fun testSignAndVerify() {
        val data = "test data"
        val password = "123456"
        val privateKey = SecurityUtils.getPrivateKey(SecurityUtils.PKCS12, BASE64_PRIVATE_KEY, password)
        val publicKey = SecurityUtils.getPublicKey(SecurityUtils.X509, BASE64_PUBLIC_KEY)

        val signature = SecurityUtils.sign(SecurityUtils.SHA1_WITH_RSA, privateKey, data.toByteArray())

        assertThat(signature).isNotNull()
                .hasSizeGreaterThan(0)
        assertThat(SecurityUtils.verify(SecurityUtils.SHA1_WITH_RSA, publicKey, data.toByteArray(), signature))
    }

    @DisplayName("test encrypt and decrypt")
    @Test
    fun testEncryptAndDecrypt() {
        val data = "test data"
        val password = "123456"
        val privateKey = SecurityUtils.getPrivateKey(SecurityUtils.PKCS12, BASE64_PRIVATE_KEY, password)
        val publicKey = SecurityUtils.getPublicKey(SecurityUtils.X509, BASE64_PUBLIC_KEY)
        val encryptedData = SecurityUtils.encrypt(SecurityUtils.RSA_ECB_PKCS1PADDING, publicKey, data.toByteArray())
        assertThat(encryptedData).isNotNull().hasSizeGreaterThan(0)
        val decryptedData = SecurityUtils.decrypt(SecurityUtils.RSA_ECB_PKCS1PADDING, privateKey, encryptedData)
        assertThat(decryptedData).isEqualTo(data.toByteArray())
    }

    @DisplayName("test encrypt and decrypt symmetrically")
    @Test
    fun testEncryptAndDecryptSymmetrically() {
        val algorithm = SecurityUtils.PBE_WITH_SHA256_AND_256BITAES_CBC_BC
        val data = "test data"
        val password = "123456"
        val salt = SecurityUtils.generateSalt(SecurityUtils.SHA1PRNG)
        val keySpec = PBEKeySpec(password.toCharArray())
        val parameterSpec = PBEParameterSpec(salt, 20)
        val key = SecurityUtils.getSecretKey(algorithm, keySpec)

        val encryptedData = SecurityUtils.encrypt(algorithm,
                key, data.toByteArray(), parameterSpec)
        assertThat(encryptedData).isNotNull().hasSizeGreaterThan(0)

        val decryptedData = SecurityUtils.decrypt(algorithm, key, encryptedData, parameterSpec)
        assertThat(decryptedData).isEqualTo(data.toByteArray())
    }

    @DisplayName("test block encrypt and decrypt")
    @Test
    fun testBlockEncryptAndDecrypt() {
        val base64PrivateKey = "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"
        val password = "100000749_272769"
        val privateKey = SecurityUtils.getPrivateKey(SecurityUtils.PKCS12, base64PrivateKey, password)
        val unEncrypted = "{\"txn_sub_type\" : \"13\",\"biz_type\" : \"0000\",\"terminal_id\" : \"100000933\",\"member_id\" : \"100000749\",\"pay_code\" : \"ABC\",\"pay_cm\" : \"2\",\"acc_no\" : \"6228480444455553333\",\"id_card_type\" : \"01\",\"id_card\" : \"320301198502169142\",\"id_holder\" : \"王宝\",\"mobile\" : \"13861190205\",\"trans_id\" : \"201609201410521111\",\"txn_amt\" : \"1.00\",\"trade_date\" : \"20160920141053\",\"trans_serial_no\" : \"11116fd1-08f3-4f04-8899-5333526b836c\"}"
        val encrypted = Hex.encodeHexString(SecurityUtils.blockEncrypt(SecurityUtils.RSA_ECB_PKCS1PADDING, privateKey,
                Base64.encodeBase64(unEncrypted.toByteArray())))
        val expectedEncrypted = "494235093656c3a6542a338d45cc0198c5e92243f9ebfafa86dda6eea780c78d9123d4c65b37ca15f3b9bb4e309660dc378fb18c41fd09006fa52750d82c187de96e233aef1dab816dce2af0a9d127b32cbad0cd90699118d3057878966cfbffb8ea7090c624c3969345ee58afd9d09a57bed963a35af1c4d652e391863138331b520557659a9637b4b36e8c4674d6ae9a4bde5820a9ec47b8e0e9df3acfc1953c9c4ca32af284325bb93941f8d5df4a55cfacc17a91e99c5127ecbec26501930f3552d72dfb41cdad08273e1632f0cad786b0d7aba8c24ea5ffde87757f5be9f847f973714952cdacdc02d62d63576eac038fd63633bcc508f4f897a98f7d0a0d4f6a81a9cfe7d1fb4840b9b8615614fb894132896d524fcdeb3e077fe5e49e13c35cdc8eeeced56d057968d5fd7474d9a2334d8819c98b8cfb432ddedd2cf53d275f2c544ef05464f238eb2e323694a895b41b27a3983c07e6a84cb1c9393c0a22f043bf0dbaddd8de9bffab5adece3ffb041d2a61098a7d99297efd1f3a957d6a8f7f3f8aaafb41f5ad28a55093c2fafc1b8140249ce6065629ca54cbb71a33a59ca3ca14ca2c33245918a911adbc6264450f2176fd466a38331fc807b9d5c47877a996f5ea32f87c7ca11a423ecc603c63bd35068144246f2adbf8c462d1a34ecf4c387b1040c95c7a501d62742b1bdf352f71556e66c371f737ce662f9d7470fa01f798dd8190d8eff075608dd8236fef5435c28b532a76be53ccba9a935dff4ef7ccc15784d33c1dd40debe574a6ddde3666c204858a3dc62e3440a352701489bb6c5a24bce917b1f0cd3ab1488c90c9e08e2f7f3f66735db3272a6b2beaa1191bb05165e80d6300df11c97d4de88d0847cebcbf2b1410cb32ce8af69d"
        assertThat(encrypted).isEqualTo(expectedEncrypted)

        val base64PublicKey = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tDQpNSUlDV1RDQ0FjS2dBd0lCQWdJR0FVM00vNlkzTUEwR0NTcUdTSWIzRFFFQkRRVUFNSEF4SFRBYkJnTlZCQU1NDQpGREV3TURBd01EYzBPVUJBTVRBd01EQXdPVE16TVJFd0R3WURWUVFIREFoemFHRnVaMmhoYVRFUk1BOEdBMVVFDQpDQXdJVTJoaGJtZElZV2t4Q3pBSkJnTlZCQVlUQWtOT01Rc3dDUVlEVlFRS0RBSmlaakVQTUEwR0ExVUVDd3dHDQpZbUZ2Wm05dk1CNFhEVEUxTURZd056QTNOVEF4TlZvWERURTFNRFl6TURBM05UQXhOVm93Y0RFZE1Cc0dBMVVFDQpBd3dVTVRBd01EQXdOelE1UUVBeE1EQXdNREE1TXpNeEVUQVBCZ05WQkFjTUNITm9ZVzVuYUdGcE1SRXdEd1lEDQpWUVFJREFoVGFHRnVaMGhoYVRFTE1Ba0dBMVVFQmhNQ1EwNHhDekFKQmdOVkJBb01BbUptTVE4d0RRWURWUVFMDQpEQVppWVc5bWIyOHdnWjh3RFFZSktvWklodmNOQVFFQkJRQURnWTBBTUlHSkFvR0JBSUhnRjZRQkk1QzZwK1ZxDQp4OCt1RnNSYlBBQzJyVFdGTHdyVWU2N0poNjFkcWsvWU5EQzJWWlVuZDd5Vzl2WnQydGRDL01uZVZIcjUvR21mDQpFakFSTHJHUjZ6NmIzckI2bjhVeUVucy9PUE03SGM0cWpqNFhTOS8xbXVoR09DNkpIdExiKy9CUUZ4OUJZQmhLDQo0c21KbzJiZC9taWpXa1UwYmNQZjBDUUpCdFRMQWdNQkFBRXdEUVlKS29aSWh2Y05BUUVOQlFBRGdZRUFUUlBqDQpZSTNCS2FsMmlRdUYyQUJFWVpMRHBqdXBiNmpZMzFnT0ZCanppR2ZTVEFINysxRkQwWkZlNWRPdzlnOUhtemNjDQpNUXNxMVVlRFpsd3dLVHVzMmdLampiaE9ZT0h4TEVQU1dnc0tNTUdRNFFzaGY4Z29UVGt1SG9Xakc5WHhaOW5iDQpoeEZsRzB5MkNNSTMwSTdQL29OVDkzZSt3blBEQWJvb3dmSlNaVDg9DQotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tDQo="
        val unDecrypted = "385f27672785256928c96b9be5c00b851d5b3f6278152e299a5474202fb5326486892c523c13805655c6b6a8dc109f45d496b880c2956f693101d451cb3d958be9d0c805b2d2f135b390ad9d893c0754a8f6de1c504d1ece472281756e16247b5c3e083f2c1f450b307ec8d73be821274ed3f1630913fbbe7490208e3dd6d1b15f3e0c0430aff716fa73a23afcbf17425430334cb026478699f95d3a42879066ae7c1aa7aa5c0433ebc28f28efde8e2cb0de685a03f3e8823f478b935b1178b728b0f115a428a5a6b5f731e3e66454de2f7004ca7475195c8aec2ec4e49985578bc6f44e388854d5d2a3ca01cde9c8ef3c78d3ef52e7b0ac758dbbcea23cc4432f741be6f9c0b3a87219664b878fc3d6a574073ed07316db2d2ab4d3810781f604611014930e5c83bb6d187fa2a223f6cdced6893f794fef839568874bf16a9499aa0e2a3fbac72c7b734a93e1bf874ec9b3eebe1ccfbd3e46cb99eb28d3ac19fdbbf549cbb6353b0a474869f1fdec076715c7b71abce31f5ab29c7f16d348ad6b89bb7fd9c65b204bc8f4e5a4f04299ed9f3a5b6fb482594a4049cd3fe1eb7a9934c4024c4bf49e850430a862e7dece99e6ac4a9810aebb9d99428bc5b29118f88e22f8dd1a32cd43acd9c5331ba04b6ae6c428a70ad189c4ee6225fb68ce4eb3910ce10201e6b6385b729cd49c634e76e2e67732e0f8cf9acbfac2b237c605728d0ed4022446bfad7bf2a3019901e4dce42b1e9d8f6affc9f6c87fc98f2dc905ee50d0aebaf5b3efd83bc9698a5380477005a4800fe8a3177b745c74828c9f542070ddfcff113669d9f41c80bfcd52a4d5ab31ae3bfd63cf3bee58e4c7087b7d75e51f6a6f60fe66872ed985ead58f923ad023a9f62213c39a2212a865eb0c"
        val publicKey = SecurityUtils.getPublicKey(SecurityUtils.X509, base64PublicKey)
        val decrypted = SecurityUtils.blockDecrypt(SecurityUtils.RSA_ECB_PKCS1PADDING, publicKey,
                Hex.decodeHex(unDecrypted)).toString(Charset.forName(StringUtils.UTF_8))
        val expectedDecrypted = "eyJiaXpfdHlwZSI6IjAwMDAiLCJkYXRhX3R5cGUiOiJqc29uIiwibWVtYmVyX2lkIjoiMTAwMDAwNzQ5IiwicmVzcF9jb2RlIjoiMDAwMCIsInJlc3BfbXNnIjoi5Lqk5piT5oiQ5YqfIiwic3VjY19hbXQiOiIxIiwidGVybWluYWxfaWQiOiIxMDAwMDA5MzMiLCJ0cmFkZV9kYXRlIjoiMjAxNjA5MjAxNDQ0MDYiLCJ0cmFuc19pZCI6IjIwMTYwOTIwMTQ0NDA1MTExMSIsInRyYW5zX25vIjoiMjAxNjA5MjAwMTEwMDAwNDAxNTU4NDA2IiwidHJhbnNfc2VyaWFsX25vIjoiOGIwZWQwMWQtMmZiZS00NGEzLWE4NjYtNDRhYTJmMGU2ZTE2IiwidHhuX3N1Yl90eXBlIjoiMTMiLCJ0eG5fdHlwZSI6IjA0MzEiLCJ2ZXJzaW9uIjoiNC4wLjAuMCJ9"
        assertThat(decrypted).isEqualTo(expectedDecrypted)
    }
}