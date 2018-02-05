package io.matchmore.sdk.api.adapters

import com.google.gson.*
import io.matchmore.sdk.api.models.*
import java.lang.reflect.Type


class DeviceTypeAdapter : JsonSerializer<Device>, JsonDeserializer<Device> {
    override fun serialize(src: Device, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return when (src) {
            is MobileDevice -> context.serialize(src, MobileDevice::class.java)
            is IBeaconDevice -> context.serialize(src, IBeaconDevice::class.java)
            is PinDevice -> context.serialize(src, PinDevice::class.java)
            else -> context.serialize(src)
        }
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Device {
        return when (context.deserialize<DeviceType>(json.asJsonObject.get("deviceType"), DeviceType::class.java)) {
            DeviceType.MOBILE_DEVICE -> context.deserialize(json, MobileDevice::class.java)
            DeviceType.PIN_DEVICE -> context.deserialize(json, PinDevice::class.java)
            DeviceType.IBEACON_DEVICE -> context.deserialize(json, IBeaconDevice::class.java)
            null -> context.deserialize(json, typeOfT)
        }
    }
}