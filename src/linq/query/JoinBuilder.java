package linq.query;

import linq.lambda.Func2;

import java.util.ArrayList;
import java.util.Collection;

public class JoinBuilder<TOne, TOther> {
    private ArrayList<TOne> one;
    private ArrayList<TOther> other;
    private Func2<TOne, TOther, Boolean> condition;

    JoinBuilder(Collection<TOne> one, Collection<TOther> other) {
        this.one = new ArrayList<>(one);
        this.other = new ArrayList<>(other);
        this.condition = (element, otherElement) -> true;
    }

    public JoinBuilder<TOne, TOther> on(Func2<TOne, TOther, Boolean> condition) {
        this.condition = condition;
        return this;
    }

    public <TResult> QueryBuilder<TResult> into(Func2<TOne, TOther, TResult> converter) {
        var elements = new ArrayList<TResult>();
        for (var element : one) {
            for (var otherElement : other) {
                if (condition.execute(element, otherElement)) {
                    elements.add(converter.execute(element, otherElement));
                }
            }
        }

        return new QueryBuilder<>(elements);
    }
}
