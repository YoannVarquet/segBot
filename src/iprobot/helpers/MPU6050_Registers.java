package iprobot.helpers;

public class MPU6050_Registers {

	public static int  MPU6050_DEFAULT_ADDRESS=0x68;

	public static int  MPU6050_RA_XG_OFFS_TC=0x00;
	public static int  MPU6050_RA_YG_OFFS_TC=0x01;
	public static int  MPU6050_RA_ZG_OFFS_TC=0x02;
	public static int  MPU6050_RA_X_FINE_GAIN=0x03;
	public static int  MPU6050_RA_Y_FINE_GAIN=0x04;
	public static int  MPU6050_RA_Z_FINE_GAIN=0x05;
	public static int  MPU6050_RA_XA_OFFS_H=0x06;
	public static int  MPU6050_RA_XA_OFFS_L_TC=0x07;
	public static int  MPU6050_RA_YA_OFFS_H=0x08;
	public static int  MPU6050_RA_YA_OFFS_L_TC=0x09;
	public static int  MPU6050_RA_ZA_OFFS_H=0x0A;
	public static int  MPU6050_RA_ZA_OFFS_L_TC=0x0B;
	public static int  MPU6050_RA_SELF_TEST_X=0x0D;
	public static int  MPU6050_RA_SELF_TEST_Y=0x0E;
	public static int  MPU6050_RA_SELF_TEST_Z=0x0F;
	public static int  MPU6050_RA_SELF_TEST_A=0x10;
	public static int  MPU6050_RA_XG_OFFS_USRH=0x13;
	public static int  MPU6050_RA_XG_OFFS_USRL=0x14;
	public static int  MPU6050_RA_YG_OFFS_USRH=0x15;
	public static int  MPU6050_RA_YG_OFFS_USRL=0x16;
	public static int  MPU6050_RA_ZG_OFFS_USRH=0x17;
	public static int  MPU6050_RA_ZG_OFFS_USRL=0x18;
	public static int  MPU6050_RA_SMPLRT_DIV=0x19;
	public static int  MPU6050_RA_CONFIG=0x1A;
	public static int  MPU6050_RA_GYRO_CONFIG=0x1B;
	public static int  MPU6050_RA_ACCEL_CONFIG=0x1C;
	public static int  MPU6050_RA_FF_THR=0x1D;
	public static int  MPU6050_RA_FF_DUR=0x1E;
	public static int  MPU6050_RA_MOT_THR=0x1F;
	public static int  MPU6050_RA_MOT_DUR=0x20;
	public static int  MPU6050_RA_ZRMOT_THR=0x21;
	public static int  MPU6050_RA_ZRMOT_DUR=0x22;
	public static int  MPU6050_RA_FIFO_EN=0x23;
	public static int  MPU6050_RA_I2C_MST_CTRL=0x24;
	public static int  MPU6050_RA_I2C_SLV0_ADDR=0x25;
	public static int  MPU6050_RA_I2C_SLV0_REG=0x26;
	public static int  MPU6050_RA_I2C_SLV0_CTRL=0x27;
	public static int  MPU6050_RA_I2C_SLV1_ADDR=0x28;
	public static int  MPU6050_RA_I2C_SLV1_REG=0x29;
	public static int  MPU6050_RA_I2C_SLV1_CTRL=0x2A;
	public static int  MPU6050_RA_I2C_SLV2_ADDR=0x2B;
	public static int  MPU6050_RA_I2C_SLV2_REG=0x2C;
	public static int  MPU6050_RA_I2C_SLV2_CTRL=0x2D;
	public static int  MPU6050_RA_I2C_SLV3_ADDR=0x2E;
	public static int  MPU6050_RA_I2C_SLV3_REG=0x2F;
	public static int  MPU6050_RA_I2C_SLV3_CTRL=0x30;
	public static int  MPU6050_RA_I2C_SLV4_ADDR=0x31;
	public static int  MPU6050_RA_I2C_SLV4_REG=0x32;
	public static int  MPU6050_RA_I2C_SLV4_DO=0x33;
	public static int  MPU6050_RA_I2C_SLV4_CTRL=0x34;
	public static int  MPU6050_RA_I2C_SLV4_DI=0x35;
	public static int  MPU6050_RA_I2C_MST_STATUS=0x36;
	public static int  MPU6050_RA_INT_PIN_CFG=0x37;
	public static int  MPU6050_RA_INT_ENABLE=0x38;
	public static int  MPU6050_RA_DMP_INT_STATUS=0x39;
	public static int  MPU6050_RA_INT_STATUS=0x3A;
	public static int  MPU6050_RA_ACCEL_XOUT_H=0x3B;
	public static int  MPU6050_RA_ACCEL_XOUT_L=0x3C;
	public static int  MPU6050_RA_ACCEL_YOUT_H=0x3D;
	public static int  MPU6050_RA_ACCEL_YOUT_L=0x3E;
	public static int  MPU6050_RA_ACCEL_ZOUT_H=0x3F;
	public static int  MPU6050_RA_ACCEL_ZOUT_L=0x40;
	public static int  MPU6050_RA_TEMP_OUT_H=0x41;
	public static int  MPU6050_RA_TEMP_OUT_L=0x42;
	public static int  MPU6050_RA_GYRO_XOUT_H=0x43;
	public static int  MPU6050_RA_GYRO_XOUT_L=0x44;
	public static int  MPU6050_RA_GYRO_YOUT_H=0x45;
	public static int  MPU6050_RA_GYRO_YOUT_L=0x46;
	public static int  MPU6050_RA_GYRO_ZOUT_H=0x47;
	public static int  MPU6050_RA_GYRO_ZOUT_L=0x48;
	public static int  MPU6050_RA_EXT_SENS_DATA_00=0x49;
	public static int  MPU6050_RA_EXT_SENS_DATA_01=0x4A;
	public static int  MPU6050_RA_EXT_SENS_DATA_02=0x4B;
	public static int  MPU6050_RA_EXT_SENS_DATA_03=0x4C;
	public static int  MPU6050_RA_EXT_SENS_DATA_04=0x4D;
	public static int  MPU6050_RA_EXT_SENS_DATA_05=0x4E;
	public static int  MPU6050_RA_EXT_SENS_DATA_06=0x4F;
	public static int  MPU6050_RA_EXT_SENS_DATA_07=0x50;
	public static int  MPU6050_RA_EXT_SENS_DATA_08=0x51;
	public static int  MPU6050_RA_EXT_SENS_DATA_09=0x52;
	public static int  MPU6050_RA_EXT_SENS_DATA_10=0x53;
	public static int  MPU6050_RA_EXT_SENS_DATA_11=0x54;
	public static int  MPU6050_RA_EXT_SENS_DATA_12=0x55;
	public static int  MPU6050_RA_EXT_SENS_DATA_13=0x56;
	public static int  MPU6050_RA_EXT_SENS_DATA_14=0x57;
	public static int  MPU6050_RA_EXT_SENS_DATA_15=0x58;
	public static int  MPU6050_RA_EXT_SENS_DATA_16=0x59;
	public static int  MPU6050_RA_EXT_SENS_DATA_17=0x5A;
	public static int  MPU6050_RA_EXT_SENS_DATA_18=0x5B;
	public static int  MPU6050_RA_EXT_SENS_DATA_19=0x5C;
	public static int  MPU6050_RA_EXT_SENS_DATA_20=0x5D;
	public static int  MPU6050_RA_EXT_SENS_DATA_21=0x5E;
	public static int  MPU6050_RA_EXT_SENS_DATA_22=0x5F;
	public static int  MPU6050_RA_EXT_SENS_DATA_23=0x60;
	public static int  MPU6050_RA_MOT_DETECT_STATUS=0x61;
	public static int  MPU6050_RA_I2C_SLV0_DO=0x63;
	public static int  MPU6050_RA_I2C_SLV1_DO=0x64;
	public static int  MPU6050_RA_I2C_SLV2_DO=0x65;
	public static int  MPU6050_RA_I2C_SLV3_DO=0x66;
	public static int  MPU6050_RA_I2C_MST_DELAY_CTRL=0x67;
	public static int  MPU6050_RA_SIGNAL_PATH_RESET=0x68;
	public static int  MPU6050_RA_MOT_DETECT_CTRL=0x69;
	public static int  MPU6050_RA_USER_CTRL=0x6A;
	public static int  MPU6050_RA_PWR_MGMT_1=0x6B;
	public static int  MPU6050_RA_PWR_MGMT_2=0x6C;
	public static int  MPU6050_RA_BANK_SEL=0x6D;
	public static int  MPU6050_RA_MEM_START_ADDR=0x6E;
	public static int  MPU6050_RA_MEM_R_W=0x6F;
	public static int  MPU6050_RA_DMP_CFG_1=0x70;
	public static int  MPU6050_RA_DMP_CFG_2=0x71;
	public static int  MPU6050_RA_FIFO_COUNTH=0x72;
	public static int  MPU6050_RA_FIFO_COUNTL=0x73;
	public static int  MPU6050_RA_FIFO_R_W=0x74;
	public static int  MPU6050_RA_WHO_AM_I=0x75;


	public static int  MPU6050_SELF_TEST_XA_1_BIT=0x07;
	public static int  MPU6050_SELF_TEST_XA_1_LENGTH=0x03;
	public static int  MPU6050_SELF_TEST_XA_2_BIT=0x05;
	public static int  MPU6050_SELF_TEST_XA_2_LENGTH=0x02;
	public static int  MPU6050_SELF_TEST_YA_1_BIT=0x07;
	public static int  MPU6050_SELF_TEST_YA_1_LENGTH=0x03;
	public static int  MPU6050_SELF_TEST_YA_2_BIT=0x03;
	public static int  MPU6050_SELF_TEST_YA_2_LENGTH=0x02;
	public static int  MPU6050_SELF_TEST_ZA_1_BIT=0x07;
	public static int  MPU6050_SELF_TEST_ZA_1_LENGTH=0x03;
	public static int  MPU6050_SELF_TEST_ZA_2_BIT=0x01;
	public static int  MPU6050_SELF_TEST_ZA_2_LENGTH=0x02;

	public static int  MPU6050_SELF_TEST_XG_1_BIT=0x04;
	public static int  MPU6050_SELF_TEST_XG_1_LENGTH=0x05;
	public static int  MPU6050_SELF_TEST_YG_1_BIT=0x04;
	public static int  MPU6050_SELF_TEST_YG_1_LENGTH=0x05;
	public static int  MPU6050_SELF_TEST_ZG_1_BIT=0x04;
	public static int  MPU6050_SELF_TEST_ZG_1_LENGTH0x05;

	public static int  MPU6050_TC_PWR_MODE_BIT=7;
	public static int  MPU6050_TC_OFFSET_BIT=6;
	public static int  MPU6050_TC_OFFSET_LENGTH=6;
	public static int  MPU6050_TC_OTP_BNK_VLD_BIT=0;

	public static int  MPU6050_VDDIO_LEVEL_VLOGIC=0;
	public static int  MPU6050_VDDIO_LEVEL_VDD=1;

	public static int  MPU6050_CFG_EXT_SYNC_SET_BIT=5;
	public static int  MPU6050_CFG_EXT_SYNC_SET_LENGTH=3;
	public static int  MPU6050_CFG_DLPF_CFG_BIT=2;
	public static int  MPU6050_CFG_DLPF_CFG_LENGTH=3;

	public static int  MPU6050_EXT_SYNC_DISABLED=0x0;
	public static int  MPU6050_EXT_SYNC_TEMP_OUT_L=0x1;
	public static int  MPU6050_EXT_SYNC_GYRO_XOUT_L=0x2;
	public static int  MPU6050_EXT_SYNC_GYRO_YOUT_L=0x3;
	public static int  MPU6050_EXT_SYNC_GYRO_ZOUT_L=0x4;
	public static int  MPU6050_EXT_SYNC_ACCEL_XOUT_L=0x5;
	public static int  MPU6050_EXT_SYNC_ACCEL_YOUT_L=0x6;
	public static int  MPU6050_EXT_SYNC_ACCEL_ZOUT_L=0x7;

	public static int  MPU6050_DLPF_BW_256=0x00;
	public static int  MPU6050_DLPF_BW_188=0x01;
	public static int  MPU6050_DLPF_BW_98=0x02;
	public static int  MPU6050_DLPF_BW_42=0x03;
	public static int  MPU6050_DLPF_BW_20=0x04;
	public static int  MPU6050_DLPF_BW_10=0x05;
	public static int  MPU6050_DLPF_BW_5=0x06;

	public static int  MPU6050_GCONFIG_FS_SEL_BIT=4;
	public static int  MPU6050_GCONFIG_FS_SEL_LENGTH= 2;

	public static int  MPU6050_GYRO_FS_250=0x00;
	public static int  MPU6050_GYRO_FS_500=0x01;
	public static int  MPU6050_GYRO_FS_1000=0x02;
	public static int  MPU6050_GYRO_FS_2000=0x03;

	public static int  MPU6050_ACONFIG_XA_ST_BIT=7;
	public static int  MPU6050_ACONFIG_YA_ST_BIT=6;
	public static int  MPU6050_ACONFIG_ZA_ST_BIT=5;
	public static int  MPU6050_ACONFIG_AFS_SEL_BIT=4;
	public static int  MPU6050_ACONFIG_AFS_SEL_LENGTH=2;
	public static int  MPU6050_ACONFIG_ACCEL_HPF_BIT=2;
	public static int  MPU6050_ACONFIG_ACCEL_HPF_LENGTH=3;

	public static int  MPU6050_ACCEL_FS_2=0x00;
	public static int  MPU6050_ACCEL_FS_4=0x01;
	public static int  MPU6050_ACCEL_FS_8=0x02;
	public static int  MPU6050_ACCEL_FS_16=0x03;

	public static int  MPU6050_DHPF_RESET=0x00;
	public static int  MPU6050_DHPF_5=0x01;
	public static int  MPU6050_DHPF_2P5=0x02;
	public static int  MPU6050_DHPF_1P25=0x03;
	public static int  MPU6050_DHPF_0P63=0x04;
	public static int  MPU6050_DHPF_HOLD=0x07;

	public static int  MPU6050_TEMP_FIFO_EN_BIT=7;
	public static int  MPU6050_XG_FIFO_EN_BIT=6;
	public static int  MPU6050_YG_FIFO_EN_BIT=5;
	public static int  MPU6050_ZG_FIFO_EN_BIT=4;
	public static int  MPU6050_ACCEL_FIFO_EN_BIT=3;
	public static int  MPU6050_SLV2_FIFO_EN_BIT=2;
	public static int  MPU6050_SLV1_FIFO_EN_BIT=1;
	public static int  MPU6050_SLV0_FIFO_EN_BIT=0;

	public static int  MPU6050_MULT_MST_EN_BIT=7;
	public static int  MPU6050_WAIT_FOR_ES_BIT=6;
	public static int  MPU6050_SLV_3_FIFO_EN_BIT=5;
	public static int  MPU6050_I2C_MST_P_NSR_BIT=4;
	public static int  MPU6050_I2C_MST_CLK_BIT=3;
	public static int  MPU6050_I2C_MST_CLK_LENGTH=4;

	public static int  MPU6050_CLOCK_DIV_348=0x0;
	public static int  MPU6050_CLOCK_DIV_333=0x1;
	public static int  MPU6050_CLOCK_DIV_320=0x2;
	public static int  MPU6050_CLOCK_DIV_308=0x3;
	public static int  MPU6050_CLOCK_DIV_296=0x4;
	public static int  MPU6050_CLOCK_DIV_286=0x5;
	public static int  MPU6050_CLOCK_DIV_276=0x6;
	public static int  MPU6050_CLOCK_DIV_267=0x7;
	public static int  MPU6050_CLOCK_DIV_258=0x8;
	public static int  MPU6050_CLOCK_DIV_500=0x9;
	public static int  MPU6050_CLOCK_DIV_471=0xA;
	public static int  MPU6050_CLOCK_DIV_444=0xB;
	public static int  MPU6050_CLOCK_DIV_421=0xC;
	public static int  MPU6050_CLOCK_DIV_400=0xD;
	public static int  MPU6050_CLOCK_DIV_381=0xE;
	public static int  MPU6050_CLOCK_DIV_364=0xF;

	public static int  MPU6050_I2C_SLV_RW_BIT=7;
	public static int  MPU6050_I2C_SLV_ADDR_BIT=6;
	public static int  MPU6050_I2C_SLV_ADDR_LENGTH=7;
	public static int  MPU6050_I2C_SLV_EN_BIT=7;
	public static int  MPU6050_I2C_SLV_BYTE_SW_BIT=6;
	public static int  MPU6050_I2C_SLV_REG_DIS_BIT=5;
	public static int  MPU6050_I2C_SLV_GRP_BIT=4;
	public static int  MPU6050_I2C_SLV_LEN_BIT=3;
	public static int  MPU6050_I2C_SLV_LEN_LENGTH=4;

	public static int  MPU6050_I2C_SLV4_RW_BIT=7;
	public static int  MPU6050_I2C_SLV4_ADDR_BIT=6;
	public static int  MPU6050_I2C_SLV4_ADDR_LENGTH=7;
	public static int  MPU6050_I2C_SLV4_EN_BIT=7;
	public static int  MPU6050_I2C_SLV4_INT_EN_BIT=6;
	public static int  MPU6050_I2C_SLV4_REG_DIS_BIT=5;
	public static int  MPU6050_I2C_SLV4_MST_DLY_BIT=4;
	public static int  MPU6050_I2C_SLV4_MST_DLY_LENGTH=5;

	public static int  MPU6050_MST_PASS_THROUGH_BIT=7;
	public static int  MPU6050_MST_I2C_SLV4_DONE_BIT=6;
	public static int  MPU6050_MST_I2C_LOST_ARB_BIT=5;
	public static int  MPU6050_MST_I2C_SLV4_NACK_BIT=4;
	public static int  MPU6050_MST_I2C_SLV3_NACK_BIT=3;
	public static int  MPU6050_MST_I2C_SLV2_NACK_BIT=2;
	public static int  MPU6050_MST_I2C_SLV1_NACK_BIT=1;
	public static int  MPU6050_MST_I2C_SLV0_NACK_BIT=0;

	public static int  MPU6050_INTCFG_INT_LEVEL_BIT=7;
	public static int  MPU6050_INTCFG_INT_OPEN_BIT=6;
	public static int  MPU6050_INTCFG_LATCH_INT_EN_BIT=5;
	public static int  MPU6050_INTCFG_INT_RD_CLEAR_BIT=4;
	public static int  MPU6050_INTCFG_FSYNC_INT_LEVEL_BIT=3;
	public static int  MPU6050_INTCFG_FSYNC_INT_EN_BIT=2;
	public static int  MPU6050_INTCFG_I2C_BYPASS_EN_BIT=1;
	public static int  MPU6050_INTCFG_CLKOUT_EN_BIT=0;

	public static int  MPU6050_INTMODE_ACTIVEHIGH0x00;
	public static int  MPU6050_INTMODE_ACTIVELOW0x01;

	public static int  MPU6050_INTDRV_PUSHPULL=0x00;
	public static int  MPU6050_INTDRV_OPENDRAIN=0x01;

	public static int  MPU6050_INTLATCH_50USPULSE=0x00;
	public static int  MPU6050_INTLATCH_WAITCLEAR=0x01;

	public static int  MPU6050_INTCLEAR_STATUSREAD=0x00;
	public static int  MPU6050_INTCLEAR_ANYREAD=0x01;

	public static int  MPU6050_INTERRUPT_FF_BIT=7;
	public static int  MPU6050_INTERRUPT_MOT_BIT=6;
	public static int  MPU6050_INTERRUPT_ZMOT_BIT=5;
	public static int  MPU6050_INTERRUPT_FIFO_OFLOW_BIT=4;
	public static int  MPU6050_INTERRUPT_I2C_MST_INT_BIT=3;
	public static int  MPU6050_INTERRUPT_PLL_RDY_INT_BIT=2;
	public static int  MPU6050_INTERRUPT_DMP_INT_BIT=1;
	public static int  MPU6050_INTERRUPT_DATA_RDY_BIT=0;

	public static int  MPU6050_DMPINT_5_BIT=5;
	public static int  MPU6050_DMPINT_4_BIT=4;
	public static int  MPU6050_DMPINT_3_BIT=3;
	public static int  MPU6050_DMPINT_2_BIT=2;
	public static int  MPU6050_DMPINT_1_BIT=1;
	public static int  MPU6050_DMPINT_0_BIT=0;

	public static int  MPU6050_MOTION_MOT_XNEG_BIT=7;
	public static int  MPU6050_MOTION_MOT_XPOS_BIT=6;
	public static int  MPU6050_MOTION_MOT_YNEG_BIT=5;
	public static int  MPU6050_MOTION_MOT_YPOS_BIT=4;
	public static int  MPU6050_MOTION_MOT_ZNEG_BIT=3;
	public static int  MPU6050_MOTION_MOT_ZPOS_BIT=2;
	public static int  MPU6050_MOTION_MOT_ZRMOT_BIT=0;

	public static int  MPU6050_DELAYCTRL_DELAY_ES_SHADOW_BIT=7;
	public static int  MPU6050_DELAYCTRL_I2C_SLV4_DLY_EN_BIT=4;
	public static int  MPU6050_DELAYCTRL_I2C_SLV3_DLY_EN_BIT=3;
	public static int  MPU6050_DELAYCTRL_I2C_SLV2_DLY_EN_BIT=2;
	public static int  MPU6050_DELAYCTRL_I2C_SLV1_DLY_EN_BIT=1;
	public static int  MPU6050_DELAYCTRL_I2C_SLV0_DLY_EN_BIT=0;

	public static int  MPU6050_PATHRESET_GYRO_RESET_BIT=2;
	public static int  MPU6050_PATHRESET_ACCEL_RESET_BIT=1;
	public static int  MPU6050_PATHRESET_TEMP_RESET_BIT=0;

	public static int  MPU6050_DETECT_ACCEL_ON_DELAY_BIT=5;
	public static int  MPU6050_DETECT_ACCEL_ON_DELAY_LENGTH=2;
	public static int  MPU6050_DETECT_FF_COUNT_BIT=3;
	public static int  MPU6050_DETECT_FF_COUNT_LENGTH=2;
	public static int  MPU6050_DETECT_MOT_COUNT_BIT=1;
	public static int  MPU6050_DETECT_MOT_COUNT_LENGTH=2;

	public static int  MPU6050_DETECT_DECREMENT_RESET=0x0;
	public static int  MPU6050_DETECT_DECREMENT_1=0x1;
	public static int  MPU6050_DETECT_DECREMENT_2=0x2;
	public static int  MPU6050_DETECT_DECREMENT_4=0x3;

	public static int  MPU6050_USERCTRL_DMP_EN_BIT=7;
	public static int  MPU6050_USERCTRL_FIFO_EN_BIT=6;
	public static int  MPU6050_USERCTRL_I2C_MST_EN_BIT=5;
	public static int  MPU6050_USERCTRL_I2C_IF_DIS_BIT=4;
	public static int  MPU6050_USERCTRL_DMP_RESET_BIT=3;
	public static int  MPU6050_USERCTRL_FIFO_RESET_BIT=2;
	public static int  MPU6050_USERCTRL_I2C_MST_RESET_BIT=1;
	public static int  MPU6050_USERCTRL_SIG_COND_RESET_BIT=0;

	public static int  MPU6050_PWR1_DEVICE_RESET_BIT=7;
	public static int  MPU6050_PWR1_SLEEP_BIT=6;
	public static int  MPU6050_PWR1_CYCLE_BIT=5;
	public static int  MPU6050_PWR1_TEMP_DIS_BIT=3;
	public static int  MPU6050_PWR1_CLKSEL_BIT=2;
	public static int  MPU6050_PWR1_CLKSEL_LENGTH=3;

	public static int  MPU6050_CLOCK_INTERNAL=0x00;
	public static int  MPU6050_CLOCK_PLL_XGYRO=0x01;
	public static int  MPU6050_CLOCK_PLL_YGYRO=0x02;
	public static int  MPU6050_CLOCK_PLL_ZGYRO=0x03;
	public static int  MPU6050_CLOCK_PLL_EXT32K=0x04;
	public static int  MPU6050_CLOCK_PLL_EXT19M=0x05;
	public static int  MPU6050_CLOCK_KEEP_RESET=0x07;

	public static int  MPU6050_PWR2_LP_WAKE_CTRL_BIT=7;
	public static int  MPU6050_PWR2_LP_WAKE_CTRL_LENGTH=2;
	public static int  MPU6050_PWR2_STBY_XA_BIT=5;
	public static int  MPU6050_PWR2_STBY_YA_BIT=4;
	public static int  MPU6050_PWR2_STBY_ZA_BIT=3;
	public static int  MPU6050_PWR2_STBY_XG_BIT=2;
	public static int  MPU6050_PWR2_STBY_YG_BIT=1;
	public static int  MPU6050_PWR2_STBY_ZG_BIT=0;

	public static int  MPU6050_WAKE_FREQ_1P25=0x0;
	public static int  MPU6050_WAKE_FREQ_2P5=0x1;
	public static int  MPU6050_WAKE_FREQ_5=0x2;
	public static int  MPU6050_WAKE_FREQ_10=0x3;

	public static int  MPU6050_BANKSEL_PRFTCH_EN_BIT=6;
	public static int  MPU6050_BANKSEL_CFG_USER_BANK_BIT=5;
	public static int  MPU6050_BANKSEL_MEM_SEL_BIT=4;
	public static int  MPU6050_BANKSEL_MEM_SEL_LENGTH=5;

	public static int  MPU6050_WHO_AM_I_BIT=6;
	public static int  MPU6050_WHO_AM_I_LENGTH=6;

	public static int  MPU6050_DMP_MEMORY_BANKS=8;
	public static int  MPU6050_DMP_MEMORY_BANK_SIZE=256;
	public static int  MPU6050_DMP_MEMORY_CHUNK_SIZE=16;
	 
}
