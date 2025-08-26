-- Users table
CREATE TABLE users (
   user_id SERIAL PRIMARY KEY,
   username VARCHAR(50) UNIQUE NOT NULL,
   email VARCHAR(100) UNIQUE NOT NULL,
   password_hash VARCHAR(255) NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Politicians table (optional, normalized)
CREATE TABLE politicians (
     politician_id SERIAL PRIMARY KEY,
     name VARCHAR(100) UNIQUE NOT NULL,
     party VARCHAR(50),
     state VARCHAR(50)
);

-- Trades table
CREATE TABLE trades (
    trade_id SERIAL PRIMARY KEY,
    politician VARCHAR(100),
    --politician_id INT REFERENCES politicians(politician_id) ON DELETE SET NULL,
    ticker_symbol VARCHAR(10),
    transaction_type VARCHAR(10) CHECK (transaction_type IN ('BUY', 'SELL')),
    amount NUMERIC(15,2),
    description VARCHAR(100),
    filing_status VARCHAR(50),
    trade_date DATE NOT NULL
    --pdf_id INT REFERENCES pdfs(pdf_id) ON DELETE SET NULL
);

-- PDFs table
CREATE TABLE pdfs (
  pdf_id SERIAL PRIMARY KEY,
  file_name VARCHAR(255) NOT NULL,
  file_data BYTEA NOT NULL,
  uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
