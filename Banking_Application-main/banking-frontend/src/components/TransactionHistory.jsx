import React, { useEffect, useState } from 'react';
import { getTransactionHistory } from '../services/api';

export default function TransactionHistory() {
  const [transactions, setTransactions] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchHistory(page);
  }, [page]);

  const fetchHistory = async (pageNumber) => {
    setLoading(true);
    setError('');
    try {
      const res = await getTransactionHistory(pageNumber, 10);

      // FIX HERE: Extract .content array from Spring Data Page object
      const data = res.data;
      if (data && Array.isArray(data.content)) {
        setTransactions(data.content);
        setTotalPages(data.totalPages || 1);
      } else if (Array.isArray(data)) {
        // Fallback if backend ever sends raw array
        setTransactions(data);
      } else {
        setTransactions([]);
      }
    } catch (err) {
      console.error('Failed to fetch transaction history:', err);
      setError('Failed to fetch transaction history');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <p>Loading transaction history...</p>;

  if (error) {
    return (
      <div>
        <p style={{ color: 'red' }}>{error}</p>
        <button onClick={() => fetchHistory(page)}>Retry</button>
      </div>
    );
  }

  return (
    <div>
      <h3>Transaction History</h3>
      {transactions.length === 0 ? (
        <p>No transactions found.</p>
      ) : (
        <>
          <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
            <thead>
              <tr style={{ backgroundColor: '#f2f2f2', textAlign: 'left' }}>
                <th style={{ padding: '8px', border: '1px solid #ddd' }}>ID</th>
                <th style={{ padding: '8px', border: '1px solid #ddd' }}>Type</th>
                <th style={{ padding: '8px', border: '1px solid #ddd' }}>Amount</th>
                <th style={{ padding: '8px', border: '1px solid #ddd' }}>Counterparty</th>
                <th style={{ padding: '8px', border: '1px solid #ddd' }}>Date</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map((tx) => (
                <tr key={tx.trans_id}>
                  <td style={{ padding: '8px', border: '1px solid #ddd' }}>{tx.trans_id}</td>
                  <td style={{ padding: '8px', border: '1px solid #ddd' }}>{tx.type}</td>
                  <td style={{ padding: '8px', border: '1px solid #ddd', color: tx.type?.includes('CREDIT') ? 'green' : 'red' }}>
                    ${tx.amount?.toFixed(2)}
                  </td>
                  <td style={{ padding: '8px', border: '1px solid #ddd' }}>{tx.counterParty || 'N/A'}</td>
                  <td style={{ padding: '8px', border: '1px solid #ddd' }}>
                    {tx.time ? new Date(tx.time).toLocaleString() : ''}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {/* Optional Pagination Controls */}
          <div style={{ marginTop: '15px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <button
              disabled={page === 0}
              onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
              style={{ padding: '5px 10px', cursor: page === 0 ? 'not-allowed' : 'pointer' }}
            >
              Previous
            </button>
            <span>Page {page + 1} of {totalPages}</span>
            <button
              disabled={page + 1 >= totalPages}
              onClick={() => setPage((prev) => prev + 1)}
              style={{ padding: '5px 10px', cursor: page + 1 >= totalPages ? 'not-allowed' : 'pointer' }}
            >
              Next
            </button>
          </div>
        </>
      )}
    </div>
  );
}