package me.jake.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Goat;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goat - Left/Right Horn State")
@Description("Returns whether or not a goat has either horn.\nCan be set.")
@Examples({"broadcast left horn of target"})
@Since("1.0.3")
public class ExprGoatHornsState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprGoatHornsState.class, Boolean.class, ExpressionType.COMBINED,
                "[the] left horn [state] of %entity%",
                "[the] right horn [state] of %entity%");
    }

    private Expression<Entity> entityExpression;
    private boolean left;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        left = matchedPattern == 0;
        return true;
    }
    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof Goat goat) {
            boolean bool;
            if (left) {
                bool = goat.hasLeftHorn();
            } else {
                bool = goat.hasRightHorn();
            }
            return new Boolean[]{bool};
        }
        return new Boolean[0];
    }
    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Boolean[].class);
        }
        return new Class[0];
    }
    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Boolean aBoolean = delta instanceof Boolean[] ? ((Boolean[]) delta)[0] : null;
        if (aBoolean == null) return;
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof Goat goat) {
            if (left) {
                goat.setLeftHorn(aBoolean);
            } else {
                goat.setRightHorn(aBoolean);
            }
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the " + (left ? "left" : "right") + " horn state of " + (e == null ? "" : entityExpression.getSingle(e));
    }
}