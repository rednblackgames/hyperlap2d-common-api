package games.rednblack.h2d.common.command;

/**
 * Typed payload for {@code ItemTransformCommand}, replacing the positional
 * {@code Array<Object>=[entity, Object[5] prev, Object[5] new]} (Phase 1 typed payloads).
 */
public record TransformPayload(int entity, TransformData prev, TransformData current) {
}