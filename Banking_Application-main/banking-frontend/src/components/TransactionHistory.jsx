import React, { useEffect, useState } from 'react';
import { getTransactionHistory } from '../services/api';

export default function TransactionHistory() {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchTransactions();
  }, []);

  const fetchTransactions = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await getTransactionHistory();
      setTransactions(res.data || []);
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data?.error || 'Failed to fetch transaction history');
      console.error('Transaction history error:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div style={styles.loadingContainer}>Loading transactions...</div>;
  }

  if (error) {
    return (
      <div style={styles.errorBox}>
        <p>{error}</p>
        <button onClick={fetchTransactions} style={styles.retryButton}>Retry</button>
      </div>
    );
  }

  return (
    <div style={styles.container}>
      <h3 style={styles.title}>Transaction History</h3>
      
      {transactions.length === 0 ? (
        <p style={styles.emptyMessage}>No transactions found</p>
      ) : (
        <div style={styles.tableWrapper}>
          <table style={styles.table}>
            <thead style={styles.tableHead}>
              <tr>
                <th style={styles.th}>Type</th>
                <th style={styles.th}>Amount</th>
                <th style={styles.th}>Counter Party</th>
                <th style={styles.th}>Date</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map((tx, idx) => (
                <tr key={idx} style={idx % 2 === 0 ? styles.rowEven : styles.rowOdd}>
                  <td style={styles.td}>
                    <span style={getTransactionTypeStyle(tx.type)}>
                      {tx.type}
                    </span>
                  </td>
                  <td style={{ ...styles.td, textAlign: 'right' }}>
                    ${tx.amount?.toFixed(2) || '0.00'}
                  </td>
                  <td style={styles.td}>{tx.counterParty || tx.receiverAccount || '-'}</td>
                  <td style={styles.td}>
                    {tx.createdAt ? new Date(tx.createdAt).toLocaleDateString() : '-'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
      
      <button onClick={fetchTransactions} style={styles.refreshButton}>
        Refresh Transactions
      </button>
    </div>
  );
}

function getTransactionTypeStyle(type) {
  const baseStyle = {
    padding: '4px 8px',
    borderRadius: '4px',
    fontSize: '12px',
    fontWeight: 'bold'
  };

  switch (type?.toUpperCase()) {
    case 'CREDIT':
    case 'CREDIT_TRANSFER':
      return { ...baseStyle, backgroundColor: '#d4edda', color: '#155724' };
    case 'DEBIT':
    case 'DEBIT_TRANSFER':
      return { ...baseStyle, backgroundColor: '#f8d7da', color: '#721c24' };
    default:
      return { ...baseStyle, backgroundColor: '#e2e3e5', color: '#383d41' };
  }
}

const styles = {
  container: {
    marginTop: '30px',
    padding: '20px',
    backgroundColor: '#f9f9f9',
    borderRadius: '8px'
  },
  title: {
    color: '#333',
    marginBottom: '20px',
    fontSize: '18px',
    fontWeight: '600'
  },
  loadingContainer: {
    textAlign: 'center',
    padding: '40px',
    color: '#666'
  },
  errorBox: {
    backgroundColor: '#f8d7da',
    color: '#721c24',
    padding: '15px',
    borderRadius: '4px',
    border: '1px solid #f5c6cb',
    marginBottom: '20px'
  },
  retryButton: {
    marginTop: '10px',
    padding: '8px 16px',
    backgroundColor: '#721c24',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer'
  },
  emptyMessage: {
    textAlign: 'center',
    color: '#999',
    padding: '40px',
    fontSize: '14px'
  },
  tableWrapper: {
    overflowX: 'auto',
    marginBottom: '20px'
  },
  table: {
    width: '100%',
    borderCollapse: 'collapse',
    backgroundColor: 'white',
    boxShadow: '0 1px 3px rgba(0,0,0,0.1)'
  },
  tableHead: {
    backgroundColor: '#007bff',
    color: 'white'
  },
  th: {
    padding: '12px',
    textAlign: 'left',
    fontWeight: '600',
    fontSize: '14px',
    borderBottom: '2px solid #dee2e6'
  },
  td: {
    padding: '12px',
    borderBottom: '1px solid #dee2e6',
    fontSize: '14px'
  },
  rowEven: {
    backgroundColor: '#f9f9f9'
  },
  rowOdd: {
    backgroundColor: 'white'
  },
  refreshButton: {
    padding: '10px 16px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    fontSize: '14px',
    fontWeight: '600'
  }
};
