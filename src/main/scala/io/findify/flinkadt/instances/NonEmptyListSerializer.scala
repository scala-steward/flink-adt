package io.findify.flinkadt.instances

import cats.data.NonEmptyList
import io.findify.flinkadt.instances.NonEmptyListSerializer.NonEmptyListSerializerSnapshot
import org.apache.flink.api.common.typeutils.{SimpleTypeSerializerSnapshot, TypeSerializer, TypeSerializerSnapshot}
import org.apache.flink.core.memory.{DataInputView, DataOutputView}

class NonEmptyListSerializer[T](child: TypeSerializer[T]) extends SimpleSerializer[NonEmptyList[T]] {
  override def createInstance(): NonEmptyList[T] = NonEmptyList.one(child.createInstance())
  override def getLength: Int = -1
  override def deserialize(source: DataInputView): NonEmptyList[T] = {
    val count = source.readInt()
    val head = child.deserialize(source)
    val tail = for {
      _ <- 0 until count-1
    } yield {
      child.deserialize(source)
    }
    NonEmptyList(head, tail.toList)
  }
  override def serialize(record: NonEmptyList[T], target: DataOutputView): Unit = {
    target.writeInt(record.size)
    record.toList.foreach(element => child.serialize(element, target))
  }
  override def snapshotConfiguration(): TypeSerializerSnapshot[NonEmptyList[T]] = new NonEmptyListSerializerSnapshot(this)

}

object NonEmptyListSerializer {
  case class NonEmptyListSerializerSnapshot[T](self: TypeSerializer[NonEmptyList[T]]) extends SimpleTypeSerializerSnapshot[NonEmptyList[T]]( () => self)
}