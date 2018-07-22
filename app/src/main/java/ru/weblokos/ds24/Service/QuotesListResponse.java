package ru.weblokos.ds24.Service;

import java.util.List;

import ru.weblokos.ds24.DB.Entity.QuoteEntity;
import ru.weblokos.ds24.Model.Quote;

public class QuotesListResponse {
    public boolean ok;
    public List<QuoteEntity> data;
}