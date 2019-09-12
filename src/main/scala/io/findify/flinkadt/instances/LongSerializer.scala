package io.findify.flinkadt.instances

import io.findify.flinkadt.instances.IntSerializer.IntSerializerSnapshot
import io.findify.flinkadt.instances.LongSerializer.LongSerializerSnapshot
import org.apache.flink.api.common.typeutils.{SimpleTypeSerializerSnapshot, TypeSerializerSnapshot}
import org.apache.flink.core.memory.{DataInputView, DataOutputView}

class LongSerializer extends SimpleSerializer[Long] {
  override def createInstance(): Long = 0
  override def getLength: Int = 8
  override def deserialize(source: DataInputView): Long = source.readLong()
  override def serialize(record: Long, target: DataOutputView): Unit = target.writeLong(record)
  override def snapshotConfiguration(): TypeSerializerSnapshot[Long] = LongSerializerSnapshot
}

object LongSerializer {
  case object LongSerializerSnapshot extends SimpleTypeSerializerSnapshot[Long](() => new LongSerializer())
}