/**
 * 
 */
package com.motorola.mobiledp.ccc.xcmp;

/**
 * @author WDGK73
 * 
 */
public enum ReplyResult {
	// 0x01 - 0x07
	Success(0), Failure(0x01), IncorrectMode(0x02), OpcodeNotSupported(0x03), InvalidParameter(
			0x04), ReplyTooBig(0x05), SecurityLocked(0x06), BundledOpcodeNotSupported(
			0x07),
	// 0x10
	LockSequenceFailure(0x10), EraseInProgress(0x10), Busy(0x10), PairDeviceFailure(
			0x10),
	// 0x11
	BitLocked(0x11), LanguagePacknotExist(0x11), PasswordVerificationFailure(
			0x11),
	// 0x12
	BitUnlocked(0x12), RadioIsLocked(0x12), TTSLanguagePackNotExist(0x12),
	// 0x13 - 0x14
	VoltageNotStable(0x13), ProgramFailure(0x14),
	// 0x16 -0x17
	TransferComplete(0x16), RequestNotRXed(0x17),
	// 0x40 -0x42
	SoftpotOperationNotSupported(0x40), SoftpotTypeNotSupported(0x41), SoftpotValueOutOfRange(
			0x42),
	// 0x80 - 0x87
	FlashWriteFailure(0x80), IshItemNotFound(0x81), IshOffsetOutOfRange(0x82), IshInsufficientPartitionSpace(
			0x83), IshPartitionDoesNotExist(0x84), IshPartitionReadOnly(0x85), IshReorgNeeded(
			0x86), 
	// 0xffffffff
	Undefined(0xffffffff);

	private int val;

	private ReplyResult(int val) {
		this.val = val;
	}

	public int value() {
		return this.val;
	}

	public static ReplyResult valueOf(int val) {
		switch (val) {
		case 0:
			return Success;
		case 1:
			return Failure;
		case 2:
			return IncorrectMode;
		case 3:
			return OpcodeNotSupported;
		case 4:
			return InvalidParameter;
		case 5:
			return ReplyTooBig;
		case 6:
			return SecurityLocked;
		case 7:
			return BundledOpcodeNotSupported;
		case 0x10:
			return LockSequenceFailure;
		case 0x11:
			return BitLocked;
		case 0x12:
			return BitUnlocked;
		case 0x13:
			return VoltageNotStable;
		case 0x14:
			return ProgramFailure;
		case 0x16:
			return TransferComplete;
		case 0x17:
			return RequestNotRXed;
		case 0x40:
			return SoftpotOperationNotSupported;
		case 0x41:
			return SoftpotTypeNotSupported;
		case 0x42:
			return SoftpotValueOutOfRange;
		case 0x80:
			return FlashWriteFailure;
		case 0x81:
			return IshItemNotFound;
		case 0x82:
			return IshOffsetOutOfRange;
		case 0x83:
			return IshInsufficientPartitionSpace;
		case 0x84:
			return IshPartitionDoesNotExist;
		case 0x85:
			return IshPartitionReadOnly;
		case 0x86:
			return IshReorgNeeded;
		default:
			return Undefined;
		}
	}

	public boolean equals(ReplyResult to) {
		return this.val == to.val;
	}
}
