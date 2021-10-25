package x1.vm.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

class StackSerializer<T>(private val dataKSerializer: KSerializer<T>) : KSerializer<Stack<T>> {
    override fun deserialize(decoder: Decoder): Stack<T> {
        val stack = Stack<T>()
        val compositeDecoder = decoder.beginStructure(descriptor)

        while (true) {
            val index = compositeDecoder.decodeElementIndex(descriptor)

            if (index == CompositeDecoder.DECODE_DONE) break

            val element = compositeDecoder.decodeSerializableElement(
                dataKSerializer.descriptor,
                index,
                dataKSerializer
            )

            stack.push(element)
        }

        compositeDecoder.endStructure(descriptor)

        return stack
    }

    override val descriptor: SerialDescriptor = listSerialDescriptor(dataKSerializer.descriptor)

    override fun serialize(encoder: Encoder, value: Stack<T>) {
        val compositeElement = encoder.beginCollection(descriptor, value.size)

        for (i in 0 until value.size) {
            compositeElement.encodeNullableSerializableElement(
                dataKSerializer.descriptor,
                i,
                dataKSerializer,
                value[i]
            )
        }

        compositeElement.endStructure(descriptor)
    }
}